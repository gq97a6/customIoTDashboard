package com.example.app.dashboard_activity

import android.content.Intent
import android.os.Bundle
import com.example.app.tiles.Tile
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.app.MainActivity
import com.example.app.databinding.DashboardActivityBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var b: DashboardActivityBinding

    private val tilesListViewModel by viewModels<TilesListViewModel> {
        TilesListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = DashboardActivityBinding.inflate(layoutInflater)
        setContentView(b.root)

        val dashboardAdapter = DashboardAdapter(this)

        val recyclerView = b.recyclerView
        recyclerView.adapter = dashboardAdapter

        tilesListViewModel.tilesLiveData.observe(this, {
            it?.let {
                dashboardAdapter.submitList(it as MutableList<Tile>)
            }
        })
    }

    private fun go(view: View) {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun adapterOnClick(tile: Tile) {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show()
    }
}