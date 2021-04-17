package com.netDashboard.tiles

import com.netDashboard.tiles.tiles.button.ButtonTile
import com.netDashboard.tiles.tiles.longButton.LongButtonTile
import com.netDashboard.tiles.tiles.slider.SliderTile
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// Handles operations on tileLiveData and holds details about it.
class TilesSource(resources: Resources) {
    private val initialTileList = listOf(
        ButtonTile(0, "", 1, 1),
        SliderTile(1, "", 1, 1),
        ButtonTile(0, "", 1, 1),
        ButtonTile(0, "", 1, 1),
        LongButtonTile(1, "", 1, 1),
        LongButtonTile(1, "", 1, 1),
        SliderTile(1, "", 1, 1),
        LongButtonTile(1, "", 1, 1),
        SliderTile(1, "", 1, 1),
        ButtonTile(0, "", 1, 1),
        SliderTile(1, "", 1, 1),
        ButtonTile(0, "", 1, 1),
        SliderTile(1, "", 1, 1),
        LongButtonTile(1, "", 1, 1),
        ButtonTile(0, "", 1, 1),
        ButtonTile(0, "", 1, 1),
        SliderTile(1, "", 1, 1),
        ButtonTile(0, "", 1, 1),
        LongButtonTile(1, "", 1, 1),
        SliderTile(1, "", 1, 1),
        LongButtonTile(1, "", 1, 1),
        LongButtonTile(1, "", 1, 1),
        LongButtonTile(1, "", 1, 1)
    )

    private val tileLiveData = MutableLiveData(initialTileList)

    fun addTile(tile: Tile) {
        val currentList = tileLiveData.value
        if (currentList == null) {
            tileLiveData.postValue(listOf(tile))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, tile)
            tileLiveData.postValue(updatedList)
        }
    }

    fun removeTile(tile: Tile) {
        val currentList = tileLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(tile)
            tileLiveData.postValue(updatedList)
        }
    }

    fun getTileList(): LiveData<List<Tile>> {
        return tileLiveData
    }

    companion object {
        private var INSTANCE: TilesSource? = null

        fun getDataSource(resources: Resources): TilesSource {
            return synchronized(TilesSource::class) {
                val newInstance = INSTANCE ?: TilesSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}