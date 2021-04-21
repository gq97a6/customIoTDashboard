package com.netDashboard.dashboard_activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netDashboard.R
import com.netDashboard.databinding.DashboardActivityBinding
import com.netDashboard.tiles.Tile

//<item name="android:windowNoTitle">true</item>
//<item name="android:windowActionBar">false</item>
//<item name="android:windowFullscreen">true</item>
//<item name="android:windowTranslucentStatus">true</item>
//<item name="android:windowLayoutInDisplayCutoutMode">shortEdges</item>

class DashboardActivity : AppCompatActivity() {
    lateinit var b: DashboardActivityBinding

    private val tilesListViewModel by viewModels<TilesListViewModel> {
        TilesListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        findViewById<RecyclerView>(R.id.recycler_view)

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

        b.move.setOnClickListener {
            if (dashboardAdapter.swapMode) {
                dashboardAdapter.swapMode = false
                b.ban.text = getString(R.string.dashboard)
            } else {
                dashboardAdapter.swapMode = true
                b.ban.text = getString(R.string.swap_mode)
            }

            for ((i, t) in dashboardAdapter.tiles.withIndex()) {
                if(t.swapFlag) {
                    dashboardAdapter.tiles[i].swapFlag = false
                    break
                }
            }
        }
    }

    //private fun go(view: View) {
    //    Intent(this, MainActivity::class.java).also {
    //        startActivity(it)
    //        finish()
    //    }
    //}
}