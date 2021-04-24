package com.netDashboard.tiles

import com.netDashboard.tiles.tiles.button.ButtonTile
import com.netDashboard.tiles.tiles.slider.SliderTile
import android.content.res.Resources
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TilesSource {
    val color = Color.parseColor("#00000000")

    private val initialTileList = listOf(
        ButtonTile("", color, 1, 1),
        SliderTile("", color, 3, 1),
        ButtonTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        ButtonTile("", color, 3, 1),
        ButtonTile("", color, 1, 1),
        SliderTile("", color, 2, 1),
        ButtonTile("", color, 2, 1),
        SliderTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        SliderTile("", color, 1, 1),
        ButtonTile("", color, 3, 1),
        SliderTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        SliderTile("", color, 2, 1),
        ButtonTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        SliderTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        ButtonTile("", color, 1, 1))

    private val tileLiveData = MutableLiveData(initialTileList)

    fun getTileList(): LiveData<List<Tile>> {
        return tileLiveData
    }

    companion object {
        private var INSTANCE: TilesSource? = null

        fun getDataSource(resources: Resources): TilesSource {
            return synchronized(TilesSource::class) {
                val newInstance = INSTANCE ?: TilesSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}