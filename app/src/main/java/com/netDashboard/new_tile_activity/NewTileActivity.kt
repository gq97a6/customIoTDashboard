package com.netDashboard.new_tile_activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netDashboard.createToast
import com.netDashboard.dashboard_activity.DashboardActivity
import com.netDashboard.databinding.NewTileActivityBinding
import com.netDashboard.tiles.TilesAdapter
import com.netDashboard.tiles.Tile

class NewTileActivity : AppCompatActivity() {
    lateinit var b: NewTileActivityBinding
    lateinit var newTileTilesAdapter: TilesAdapter

    private val tilesListViewModel by viewModels<NewTileViewModel> {
        NewTileViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = NewTileActivityBinding.inflate(layoutInflater)
        setContentView(b.root)

        setupRecyclerView()

        newTileTilesAdapter.getTileOnClickLiveData().observe(this, { tileId ->
            if(tileId >= 0) {
                createToast(this, "Tile added: $tileId")

                Intent(this, DashboardActivity::class.java).also {
                    finish()
                    startActivity(it)
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val spanCount = 3
        newTileTilesAdapter = TilesAdapter(this, spanCount, "add")
        b.recyclerView.adapter = newTileTilesAdapter

        val layoutManager = GridLayoutManager(this, spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return newTileTilesAdapter.tiles[position].x
            }
        }

        b.recyclerView.layoutManager = layoutManager
        newTileTilesAdapter.submitList(tilesListViewModel.tilesData as MutableList<Tile>)
    }
}