package com.netDashboard.new_tile_activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netDashboard.databinding.NewTileActivityBinding
import com.netDashboard.tiles.Adapter
import com.netDashboard.tiles.Tile

class NewTileActivity : AppCompatActivity() {
    lateinit var b: NewTileActivityBinding
    lateinit var recyclerView: RecyclerView
    lateinit var newTileAdapter: Adapter

    private val tilesListViewModel by viewModels<NewTileViewModel> {
        NewTileViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = NewTileActivityBinding.inflate(layoutInflater)
        setContentView(b.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val spanCount = 3
        newTileAdapter = Adapter(this, spanCount)

        recyclerView = b.recyclerView
        recyclerView.adapter = newTileAdapter

        val layoutManager = GridLayoutManager(this, spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return newTileAdapter.tiles[position].x
            }
        }

        recyclerView.layoutManager = layoutManager
        newTileAdapter.submitList(tilesListViewModel.tilesData as MutableList<Tile>)
    }
}