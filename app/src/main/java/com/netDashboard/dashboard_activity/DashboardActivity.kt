package com.netDashboard.dashboard_activity

import android.R.id.message
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
import com.netDashboard.tiles.Tile
import com.netDashboard.toPx
import new_tile_activity.NewTileActivity


class DashboardActivity : AppCompatActivity() {
    lateinit var b: DashboardActivityBinding

    private val tilesListViewModel by viewModels<TilesListViewModel> {
        TilesListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = DashboardActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        val spanCount = 3
        val dashboardAdapter = DashboardAdapter(this, spanCount)

        val recyclerView = b.recyclerView
        recyclerView.adapter = dashboardAdapter

        val layoutManager = GridLayoutManager(this, spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return dashboardAdapter.tiles[position].x
            }
        }

        recyclerView.layoutManager = layoutManager

        tilesListViewModel.tilesLiveData.observe(this, {
            it?.let {
                dashboardAdapter.submitList(it as MutableList<Tile>)
            }
        })

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
                var deleted = false
                val tiles = dashboardAdapter.tiles.toMutableList()
                for ((i, _) in dashboardAdapter.tiles.withIndex()) {

                    if (dashboardAdapter.tiles[i].flag()) {
                        tiles.removeAt(i)
                        deleted = true
                    }
                }

                if(!deleted) {
                    createToast(this, "Mark tile, click again.", 1)
                } else {
                    val snackbar = Snackbar.make(b.root, "Are you sure? Wait to dismiss.", Snackbar.LENGTH_LONG).margins().setAction(
                        "YES"
                    ) {
                        dashboardAdapter.tiles = tiles
                        dashboardAdapter.notifyDataSetChanged()
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

                createToast(this, "Mark tile, click again.", 1)
            }
        }

        b.add.setOnClickListener {
            //moveIndicator(-120.toPx().toFloat())

            Intent(this, NewTileActivity::class.java).also {
                startActivity(it)
            }
        }
    }

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