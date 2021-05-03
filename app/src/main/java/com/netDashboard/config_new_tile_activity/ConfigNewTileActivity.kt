package com.netDashboard.config_new_tile_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import com.netDashboard.dashboard_activity.DashboardActivity
import com.netDashboard.databinding.ConfigNewTileActivityBinding
import com.netDashboard.tiles.Tile
import com.netDashboard.tiles.TilesSource

class ConfigNewTileActivity : AppCompatActivity() {
    lateinit var b: ConfigNewTileActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ConfigNewTileActivityBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.width.value = 1f
        b.height.value = 1f

        b.width.valueFrom = 1f
        b.height.valueFrom = 1f

        b.width.valueTo = 3f
        b.height.valueTo = 10f

        val tileId = intent.getIntExtra("tileId", 0)
        val tile = TilesSource().getTileList()[tileId]

        b.tileType.text = tile.name

        b.push.setOnClickListener {
            configTile(tile)

            Intent(this, DashboardActivity::class.java).also {
                finish()
                startActivity(it)
            }
        }

        b.height.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                if (b.height.value == 1f) {
                    b.width.isEnabled = true
                    b.warning.visibility = View.INVISIBLE
                } else {
                    b.warning.visibility = View.VISIBLE
                    b.width.isEnabled = false
                    b.width.value = 3f
                }
            }
        })
    }

    private fun configTile(tile: Tile) {
        tile.width = b.width.value.toInt()
        tile.height = b.height.value.toInt()

        var list = TilesSource().getList(filesDir.canonicalPath + "/tileList")

        if (list == null) {
            list = listOf(tile)
        } else {
            list = list.toMutableList()
            list.add(tile)
            list = list.toList()
        }

        TilesSource().saveList(list, filesDir.canonicalPath + "/tileList")
    }
}