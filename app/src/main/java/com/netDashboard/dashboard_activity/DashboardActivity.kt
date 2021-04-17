package com.netDashboard.dashboard_activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import com.netDashboard.databinding.DashboardActivityBinding
import com.netDashboard.tiles.Tile
import java.util.*


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

        //val layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
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

        b.ban.setOnClickListener {
            Collections.swap(dashboardAdapter.tiles, 0, 6)
            dashboardAdapter.notifyItemMoved(0, 6)
        }
    }

    //private fun go(view: View) {
    //    Intent(this, MainActivity::class.java).also {
    //        startActivity(it)
    //        finish()
    //    }
    //}
}