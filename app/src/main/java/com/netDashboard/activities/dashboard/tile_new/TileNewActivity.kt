package com.netDashboard.activities.dashboard.tile_new

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.netDashboard.activities.dashboard.DashboardActivity
import com.netDashboard.activities.dashboard.tile_properties.TilePropertiesActivity
import com.netDashboard.app_on.AppOn
import com.netDashboard.click
import com.netDashboard.dashboard.Dashboard
import com.netDashboard.dashboard.Dashboard.Companion.byId
import com.netDashboard.databinding.ActivityTileNewBinding
import com.netDashboard.globals.G
import com.netDashboard.globals.G.dashboards
import com.netDashboard.tile.Tile
import com.netDashboard.tile.types.button.ButtonTile
import com.netDashboard.tile.types.button.TextTile
import com.netDashboard.tile.types.slider.SliderTile

class TileNewActivity : AppCompatActivity() {
    private lateinit var b: ActivityTileNewBinding

    private var dashboardId: Long = 0
    private lateinit var dashboard: Dashboard

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppOn.create(this)

        dashboardId = intent.getLongExtra("dashboardId", 0)
        dashboard = dashboards.byId(dashboardId)

        b = ActivityTileNewBinding.inflate(layoutInflater)
        G.theme.apply(this, b.root)
        setContentView(b.root)

        b.tnButton.setOnClickListener {
            b.tnButtonRipple.click()
            addTile(ButtonTile())
        }

        b.tnSlider.setOnClickListener {
            b.tnSliderRipple.click()
            addTile(SliderTile())
        }

        b.tnText.setOnClickListener {
            b.tnTextRipple.click()
            addTile(TextTile())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppOn.destroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        Intent(this, DashboardActivity::class.java).also {
            it.putExtra("dashboardId", dashboardId)
            startActivity(it)
        }
    }

    override fun onPause() {
        super.onPause()
        AppOn.pause()
    }

    private var isDone = false
    private fun addTile(tile: Tile) {
        if (isDone) return
        isDone = true

        tile.dashboard = dashboard
        dashboard.tiles.add(tile)

        Intent(this, TilePropertiesActivity::class.java).also {
            it.putExtra("dashboardId", dashboardId)
            it.putExtra("tileIndex", dashboard.tiles.lastIndex)
            startActivity(it)
        }
    }
}