package com.example.app.dashboard_activity

import android.os.Bundle
import com.example.app.tiles.Tile
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app.databinding.DashboardActivityBinding
import com.example.app.tiles.tilesTypesList
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
        recyclerView.layoutManager = GridLayoutManager(this, spanCount)

        tilesListViewModel.tilesLiveData.observe(this, {
            it?.let {
                dashboardAdapter.submitList(it as MutableList<Tile>)
            }
        })

        b.ban.setOnLongClickListener {
            Collections.swap(dashboardAdapter.tiles, 0, 1)
            dashboardAdapter.notifyItemMoved(0, 1)

            dashboardAdapter.tiles[2] = tilesTypesList()[0]
            dashboardAdapter.notifyItemChanged(2)

            return@setOnLongClickListener true
        }
    }

    //private fun go(view: View) {
    //    Intent(this, MainActivity::class.java).also {
    //        startActivity(it)
    //        finish()
    //    }
    //}
}