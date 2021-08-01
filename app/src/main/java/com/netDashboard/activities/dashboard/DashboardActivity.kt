package com.netDashboard.activities.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.netDashboard.R
import com.netDashboard.activities.MainActivity
import com.netDashboard.activities.dashboard.properties.PropertiesActivity
import com.netDashboard.activities.dashboard.tile_new.TileNewActivity
import com.netDashboard.app_on_destroy.AppOnDestroy
import com.netDashboard.dashboard.Dashboard
import com.netDashboard.dashboard.Dashboards
import com.netDashboard.databinding.ActivityDashboardBinding
import com.netDashboard.foreground_service.ForegroundService
import com.netDashboard.foreground_service.ForegroundServiceHandler
import com.netDashboard.getScreenHeight
import com.netDashboard.tile.TileGridLayoutManager
import com.netDashboard.tile.TilesAdapter
import com.netDashboard.toPx
import java.util.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var b: ActivityDashboardBinding

    private lateinit var dashboardName: String
    private lateinit var dashboard: Dashboard

    lateinit var adapter: TilesAdapter

    private lateinit var service: ForegroundService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(b.root)

        val foregroundServiceHandler = ForegroundServiceHandler(this)
        foregroundServiceHandler.start()
        foregroundServiceHandler.bind()

        foregroundServiceHandler.service.observe(this, { s ->
            if (s != null) {
                service = s
                onServiceReady()
            }
        })

        dashboardName = intent.getStringExtra("dashboardName") ?: ""
        dashboard = Dashboards.get(dashboardName)

        setupRecyclerView()

        //Set dashboard tag name
        b.dTagName.text = dashboard.dashboardTagName.uppercase(Locale.getDefault())

        //Set dashboard status
        dashboard.daemonGroup?.mqttd?.let {
            it.conHandler.isDone.observe(this) { isDone ->
                b.dTagStatus.text = getString(
                    if (!dashboard.mqttEnabled) {
                        R.string.d_disconnected
                    } else {
                        if (it.client.isConnected) {
                            R.string.d_connected
                        } else {
                            if (isDone) {
                                R.string.d_failed
                            } else {
                                R.string.d_attempting
                            }
                        }
                    }
                )
            }
        }

        b.dTouch.setOnClickListener {
            touchOnClick()
        }

        b.dProperties.setOnClickListener {
            propertiesOnClick()
        }

        b.dEdit.setOnClickListener {
            editOnClick()
        }

        b.dSwap.setOnClickListener {
            swapOnClick()
        }

        b.dRemove.setOnClickListener {
            removeOnClick()
        }

        b.dAdd.setOnClickListener {
            addOnClick()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        if (adapter.isEdit) {
            b.dEdit.callOnClick()
        }

        adapter.notifyDataSetChanged()
    }

    override fun onPause() {

        dashboard.tiles = adapter.tiles
        Dashboards.save(dashboardName)

        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppOnDestroy.call()
    }

    override fun onBackPressed() {
        if (adapter.isEdit) {
            b.dTouch.callOnClick()
        } else {
            super.onBackPressed()

            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private fun onServiceReady() {
    }

    //----------------------------------------------------------------------------------------------

    private fun setupRecyclerView() {
        val spanCount = dashboard.spanCount

        adapter = TilesAdapter(this, spanCount, "", dashboardName)
        adapter.setHasStableIds(true)

        b.dRecyclerView.adapter = adapter
        b.dRecyclerView.setItemViewCacheSize(20)

        val layoutManager = TileGridLayoutManager(this, spanCount)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val t = adapter.tiles[position]
                return when {
                    t.height != 1 || t.width > spanCount -> spanCount
                    else -> t.width
                }
            }
        }

        b.dRecyclerView.layoutManager = layoutManager


        adapter.submitList(dashboard.tiles.toMutableList())
        adapter.isEdit = false

        if (adapter.itemCount == 0) {
            b.dPlaceholder.visibility = View.VISIBLE
        }
    }

    //----------------------------------------------------------------------------------------------

    private fun touchOnClick() {
        adapter.isEdit = !adapter.isEdit

        if (!adapter.isEdit) {
            b.dRecyclerView.suppressLayout(false)
            dashboard.tiles = adapter.tiles
        } else {
            highlightOnly(b.dEdit)

            adapter.editMode = true

            for (t in adapter.tiles) {
                t.isEdit = true
                t.flag()
            }
        }

        if (adapter.isEdit) {
            b.dBar.y = getScreenHeight().toFloat()

            b.dBar.visibility = View.VISIBLE

            b.dBar.animate()
                .translationY(0f)
                .setInterpolator(AccelerateDecelerateInterpolator())?.duration = 400
        } else {

            b.dBar.animate()
                ?.y(getScreenHeight().toFloat())
                ?.setInterpolator(AccelerateDecelerateInterpolator())?.duration = 400
        }
    }

    //----------------------------------------------------------------------------------------------

    private fun propertiesOnClick() {
        Intent(this, PropertiesActivity::class.java).also {
            it.putExtra("dashboardName", dashboardName)
            startActivity(it)
            finish()
        }
    }

    //----------------------------------------------------------------------------------------------

    private fun editOnClick() {
        if (!adapter.isEdit) return

        highlightOnly(b.dEdit)
        //createToast(this, getString(R.string.d_edit), 1)

        adapter.editMode = true
    }

    //----------------------------------------------------------------------------------------------

    private fun swapOnClick() {
        if (!adapter.isEdit) return

        highlightOnly(b.dSwap)
        //createToast(this, getString(R.string.d_swap), 1)

        adapter.swapMode = true
    }

    //----------------------------------------------------------------------------------------------

    private fun removeOnClick() {
        if (!adapter.isEdit) return

        highlightOnly(b.dRemove)

        if (!adapter.removeMode) {

            adapter.removeMode = true

            //createToast(this, getString(R.string.d_remove))
        } else {

            var toDelete = false

            for (t in adapter.tiles) {
                if (t.flag == "remove") {
                    toDelete = true
                    break
                }
            }

            if (!toDelete) {
                //createToast(this, getString(R.string.d_remove), 1)
            } else {

                @SuppressLint("ShowToast")
                val snackbar = Snackbar.make(
                    b.root,
                    getString(R.string.snackbar_confirmation),
                    Snackbar.LENGTH_LONG
                ).setAction("YES") {

                    for ((i, t) in adapter.tiles.withIndex()) {
                        if (t.flag == "remove") {
                            adapter.tiles.removeAt(i)
                            dashboard.tiles = adapter.tiles

                            adapter.notifyDataSetChanged()

                            if (adapter.itemCount == 0) {
                                b.dPlaceholder.visibility = View.VISIBLE
                            }

                            break
                        }
                    }
                }

                val snackBarView = snackbar.view
                snackBarView.translationY = -60.toPx().toFloat()
                snackbar.show()
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    private fun addOnClick() {
        Intent(this, TileNewActivity::class.java).also {
            it.putExtra("dashboardName", dashboardName)
            startActivity(it)
            finish()
        }
    }

    //----------------------------------------------------------------------------------------------

    private fun highlightOnly(button: Button) {
        b.dRemove.alpha = 0.4f
        b.dSwap.alpha = 0.4f
        b.dEdit.alpha = 0.4f
        button.alpha = 1f
    }

    fun clickTouch(v: View) = b.dTouch.click()
    fun clickProperties(v: View) = b.dProperties.click()
    fun clickAdd(v: View) = b.dAdd.click()
    fun clickRemove(v: View) = b.dRemove.click()
    fun clickSwap(v: View) = b.dSwap.click()
    fun clickEdit(v: View) = b.dEdit.click()

    private fun Button.click() {
        this.performClick()
        this.isPressed = true
        this.invalidate()
        this.isPressed = false
        this.invalidate()
    }
}