package com.netDashboard.dashboard_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.netDashboard.R
import com.netDashboard.createToast
import com.netDashboard.databinding.DashboardActivityBinding
import com.netDashboard.margins
import com.netDashboard.new_tile_activity.NewTileActivity
import com.netDashboard.tiles.Tile
import com.netDashboard.tiles.TilesAdapter
import com.netDashboard.tiles.TilesSource
import com.netDashboard.toPx

class DashboardActivity : AppCompatActivity() {
    lateinit var b: DashboardActivityBinding
    lateinit var dashboardTilesAdapter: TilesAdapter

    private val dashboardViewModel by viewModels<DashboardViewModel> {
        DashboardViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = DashboardActivityBinding.inflate(layoutInflater)
        setContentView(b.root)

        setupRecyclerView()

        b.edit.setOnClickListener {
            dashboardTilesAdapter.swapMode =
                !(dashboardTilesAdapter.swapMode || dashboardTilesAdapter.removeMode)

            dashboardTilesAdapter.removeMode = false

            if (dashboardTilesAdapter.swapMode) {
                b.ban.text = getString(R.string.swap_mode)

                b.remove.visibility = View.VISIBLE
                b.add.visibility = View.VISIBLE
                b.set.setBackgroundResource(R.drawable.button_swap)
            } else {
                b.ban.text = getString(R.string.dashboard)

                b.remove.visibility = View.GONE
                b.add.visibility = View.GONE

                b.set.setBackgroundResource(R.drawable.button_more)

                val saveMe = dashboardTilesAdapter.tiles.toList()
                TilesSource().saveList(saveMe, filesDir.canonicalPath + "/tileList")
            }

            for ((i, _) in dashboardTilesAdapter.tiles.withIndex()) {
                dashboardTilesAdapter.tiles[i].editMode(dashboardTilesAdapter.swapMode)
                dashboardTilesAdapter.tiles[i].flag(false)
            }

            dashboardTilesAdapter.notifyDataSetChanged()
        }

        b.set.setOnClickListener {
            if (dashboardTilesAdapter.removeMode) {
                dashboardTilesAdapter.removeMode = false
                dashboardTilesAdapter.swapMode = true
                b.ban.text = getString(R.string.swap_mode)

                for ((i, _) in dashboardTilesAdapter.tiles.withIndex()) {
                    dashboardTilesAdapter.tiles[i].editMode(true)
                    dashboardTilesAdapter.tiles[i].flag(false)
                }
            }
            else if(!dashboardTilesAdapter.swapMode) {
                createToast(this, "RESTORE")
            }
        }

        b.remove.setOnClickListener {
            if (dashboardTilesAdapter.removeMode) {
                var toDelete = false

                for ((i, _) in dashboardTilesAdapter.tiles.withIndex()) {

                    if (dashboardTilesAdapter.tiles[i].flag()) {
                        toDelete = true
                        break
                    }
                }

                if (!toDelete) {
                    createToast(this, "Mark tile, click again.", 1)
                } else {

                    val snackbar = Snackbar.make(
                        b.root,
                        "Are you sure? Wait to dismiss.",
                        Snackbar.LENGTH_LONG
                    ).margins().setAction("YES") {

                        for ((i, _) in dashboardTilesAdapter.tiles.withIndex()) {

                            if (dashboardTilesAdapter.tiles[i].flag()) {
                                dashboardTilesAdapter.tiles.removeAt(i)

                                dashboardTilesAdapter.notifyItemRemoved(i)
                                dashboardTilesAdapter.notifyItemRangeChanged(
                                    i,
                                    dashboardTilesAdapter.itemCount - i
                                )
                                break
                            }
                        }
                    }

                    val snackBarView = snackbar.view
                    snackBarView.translationY = -20.toPx().toFloat()
                    snackbar.show()
                }
            } else if (dashboardTilesAdapter.swapMode) {
                dashboardTilesAdapter.swapMode = false
                dashboardTilesAdapter.removeMode = true
                b.ban.text = getString(R.string.remove_mode)

                for ((i, _) in dashboardTilesAdapter.tiles.withIndex()) {
                    dashboardTilesAdapter.tiles[i].flag(false)
                }

                createToast(this, "Mark tile, click again.")
            }
        }

        b.add.setOnClickListener {
            Intent(this, NewTileActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onPause() {
        val saveMe = dashboardTilesAdapter.tiles.toList()
        TilesSource().saveList(saveMe, filesDir.canonicalPath + "/tileList")

        super.onPause()
    }

    private fun setupRecyclerView() {
        val spanCount = 3
        dashboardTilesAdapter = TilesAdapter(this, spanCount)
        b.recyclerView.adapter = dashboardTilesAdapter

        val layoutManager = GridLayoutManager(this, spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return dashboardTilesAdapter.tiles[position].width
            }
        }

        b.recyclerView.layoutManager = layoutManager

        if(TilesSource().getList(filesDir.canonicalPath + "/tileList") != null) {
            dashboardTilesAdapter.submitList(TilesSource().getList(filesDir.canonicalPath + "/tileList") as MutableList<Tile>)
        } else {
            dashboardTilesAdapter.submitList(mutableListOf())
        }
    }

    //ObjectAnimator.ofFloat(b.indicator, "translationX", distance)
    //.apply {
    //    this.duration = duration
    //    start()
    //}

    //Handler(Looper.getMainLooper()).postDelayed({
    //    moveIndicator(0f, 300)
    //}, 400)
}