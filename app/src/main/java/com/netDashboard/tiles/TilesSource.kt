package com.netDashboard.tiles

import com.netDashboard.tiles.tiles.button.ButtonTile
import com.netDashboard.tiles.tiles.longButton.LongButtonTile
import com.netDashboard.tiles.tiles.slider.SliderTile
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*
import kotlin.random.Random.Default.nextInt

// Handles operations on tileLiveData and holds details about it.
class TilesSource(resources: Resources) {
    private val initialTileList = listOf(
        ButtonTile(Random().nextLong(), "", 1, 1),
        SliderTile(Random().nextLong(), "", 2, 1),
        ButtonTile(Random().nextLong(), "", 1, 1),
        ButtonTile(Random().nextLong(), "", 1, 1),
        LongButtonTile(Random().nextLong(), "", 3, 3),
        LongButtonTile(Random().nextLong(), "", 1, 1),
        SliderTile(Random().nextLong(), "", 2, 1),
        LongButtonTile(Random().nextLong(), "", 2, 1),
        SliderTile(Random().nextLong(), "", 1, 1),
        ButtonTile(Random().nextLong(), "", 1, 1),
        SliderTile(Random().nextLong(), "", 1, 1),
        ButtonTile(Random().nextLong(), "", 1, 1),
        SliderTile(Random().nextLong(), "", 1, 1),
        LongButtonTile(Random().nextLong(), "", 1, 1),
        ButtonTile(Random().nextLong(), "", 1, 1),
        ButtonTile(Random().nextLong(), "", 1, 1),
        SliderTile(Random().nextLong(), "", 1, 1),
        ButtonTile(Random().nextLong(), "", 1, 1),
        LongButtonTile(Random().nextLong(), "", 1, 1),
        SliderTile(Random().nextLong(), "", 1, 1),
        LongButtonTile(Random().nextLong(), "", 1, 1),
        LongButtonTile(Random().nextLong(), "", 1, 1),
        LongButtonTile(Random().nextLong(), "", 1, 1)
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