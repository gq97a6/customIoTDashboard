package com.netDashboard.dashboard_activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.netDashboard.R
import com.netDashboard.databinding.DashboardActivityBinding
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
            dashboardAdapter.swapMode = !dashboardAdapter.swapMode

            if (dashboardAdapter.swapMode) {
                b.ban.text = getString(R.string.edit_mode)

                b.remove.visibility = View.VISIBLE
                b.add.visibility = View.VISIBLE
                b.indicator.visibility = View.VISIBLE

                moveIndicator(0f)

                b.set.setBackgroundResource(R.drawable.button_swap)
            } else {
                b.ban.text = getString(R.string.dashboard)

                b.remove.visibility = View.GONE
                b.add.visibility = View.GONE
                b.indicator.visibility = View.GONE

                b.set.setBackgroundResource(R.drawable.button_more)
            }

            for ((i, _) in dashboardAdapter.tiles.withIndex()) {
                dashboardAdapter.tiles[i].swapMode(dashboardAdapter.swapMode)
                dashboardAdapter.tiles[i].swapReady(false)
            }
        }

        b.set.setOnClickListener {
            if (dashboardAdapter.swapMode) {
                moveIndicator(0f)
            }
        }

        b.remove.setOnClickListener {
            if (dashboardAdapter.swapMode) {
                moveIndicator(-60.toPx().toFloat())
            }
        }

        b.add.setOnClickListener {
            if (dashboardAdapter.swapMode) {
                //moveIndicator(-120.toPx().toFloat())

                Intent(this, NewTileActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    private fun moveIndicator(distance: Float) {

        ObjectAnimator.ofFloat(b.indicator, "translationX", distance)
            .apply {
                duration = 100
                start()
            }
    }

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