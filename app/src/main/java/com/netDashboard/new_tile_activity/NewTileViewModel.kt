package com.netDashboard.new_tile_activity

import androidx.lifecycle.ViewModel
import com.netDashboard.tiles.TilesSource

class NewTileViewModel : ViewModel() {
    val tilesData = TilesSource().getTileList()
}