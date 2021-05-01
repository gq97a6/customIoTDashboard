package com.netDashboard.dashboard_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.netDashboard.R
import com.netDashboard.createToast
import com.netDashboard.databinding.DashboardActivityBinding
import com.netDashboard.margins
import com.netDashboard.new_tile_activity.NewTileActivity
import com.netDashboard.tiles.Adapter
import com.netDashboard.tiles.Tile
import com.netDashboard.toPx

class DashboardActivity : AppCompatActivity() {
    lateinit var b: DashboardActivityBinding
    lateinit var recyclerView: RecyclerView
    lateinit var dashboardAdapter: Adapter

    private val dashboardViewModel by viewModels<DashboardViewModel> {
        DashboardViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = DashboardActivityBinding.inflate(layoutInflater)
        setContentView(b.root)

        setupRecyclerView()

        b.edit.setOnClickListener {
            dashboardAdapter.swapMode = !(dashboardAdapter.swapMode || dashboardAdapter.removeMode)

            dashboardAdapter.removeMode = false

            if (dashboardAdapter.swapMode) {
                b.ban.text = getString(R.string.swap_mode)

                b.remove.visibility = View.VISIBLE
                b.add.visibility = View.VISIBLE
                b.set.setBackgroundResource(R.drawable.button_swap)
            } else {
                b.ban.text = getString(R.string.dashboard)

                b.remove.visibility = View.GONE
                b.add.visibility = View.GONE

                b.set.setBackgroundResource(R.drawable.button_more)
            }

            for ((i, _) in dashboardAdapter.tiles.withIndex()) {
                dashboardAdapter.tiles[i].editMode(dashboardAdapter.swapMode)
                dashboardAdapter.tiles[i].flag(false)
            }
        }

        b.set.setOnClickListener {
            if (dashboardAdapter.removeMode) {
                dashboardAdapter.removeMode = false
                dashboardAdapter.swapMode = true
                b.ban.text = getString(R.string.swap_mode)

                for ((i, _) in dashboardAdapter.tiles.withIndex()) {
                    dashboardAdapter.tiles[i].editMode(true)
                    dashboardAdapter.tiles[i].flag(false)
                }
            }
        }

        b.remove.setOnClickListener {
            if (dashboardAdapter.removeMode) {
                var toDelete = false

                for ((i, _) in dashboardAdapter.tiles.withIndex()) {

                    if (dashboardAdapter.tiles[i].flag()) {
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
                        Snackbar.LENGTH_LONG).margins().setAction("YES") {

                        for ((i, _) in dashboardAdapter.tiles.withIndex()) { //TODO

                            if (dashboardAdapter.tiles[i].flag()) {
                                dashboardAdapter.tiles.removeAt(i)

                                dashboardAdapter.notifyItemRemoved(i)
                                dashboardAdapter.notifyItemRangeChanged(i, dashboardAdapter.itemCount-i)
                                break
                            }
                        }
                    }

                    val snackBarView = snackbar.view
                    snackBarView.translationY = -20.toPx().toFloat()
                    snackbar.show()
                }
            } else if (dashboardAdapter.swapMode) {
                dashboardAdapter.swapMode = false
                dashboardAdapter.removeMode = true
                b.ban.text = getString(R.string.remove_mode)

                for ((i, _) in dashboardAdapter.tiles.withIndex()) {
                    dashboardAdapter.tiles[i].flag(false)
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

    private fun setupRecyclerView() {
        val spanCount = 3
        dashboardAdapter = Adapter(this, spanCount)

        recyclerView = b.recyclerView
        recyclerView.adapter = dashboardAdapter

        val layoutManager = GridLayoutManager(this, spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return dashboardAdapter.tiles[position].x
            }
        }

        recyclerView.layoutManager = layoutManager
        dashboardAdapter.submitList(dashboardViewModel.tilesData as MutableList<Tile>)
    }

    //tilesListViewModel.tilesLiveData.observe(this, {
    //    it?.let {
    //        dashboardAdapter.submitList(it as MutableList<Tile>)
    //    }
    //})

    //ObjectAnimator.ofFloat(b.indicator, "translationX", distance)
    //.apply {
    //    this.duration = duration
    //    start()
    //}
    //
    //Handler(Looper.getMainLooper()).postDelayed({
    //    moveIndicator(0f, 300)
    //}, 400)

    //Intent(this, NewTileActivity::class.java).also {
    //    startActivity(it)
    //}

    //tilesListViewModel.dataSource.removeTile()

    //private fun go(view: View) {
    //    Intent(this, MainActivity::class.java).also {
    //        startActivity(it)
    //        finish()
    //    }
    //}
}