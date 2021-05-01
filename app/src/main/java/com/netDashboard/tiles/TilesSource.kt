package com.netDashboard.tiles

import android.graphics.Color
import com.netDashboard.tiles.tiles_types.button.ButtonTile
import com.netDashboard.tiles.tiles_types.slider.SliderTile

class TilesSource {
    val color = Color.parseColor("#00000000")

    private val tileList = listOf(
        ButtonTile("", color, 3, 1),
        SliderTile("", color, 3, 1)
    )

    private val initialTileList = listOf(
        ButtonTile("", color, 1, 1),
        SliderTile("", color, 1, 1),
        ButtonTile("", color, 3, 2),
        ButtonTile("", color, 2, 1),
        ButtonTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        SliderTile("", color, 1, 1),
        ButtonTile("", color, 3, 1),
        SliderTile("", color, 1, 1),
        ButtonTile("", color, 2, 1),
        SliderTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        SliderTile("", color, 1, 1),
        ButtonTile("", color, 2, 1),
        ButtonTile("", color, 1, 1),
        ButtonTile("", color, 1, 1),
        SliderTile("", color, 2, 1),
        ButtonTile("", color, 2, 1),
        ButtonTile("", color, 3, 1),
        SliderTile("", color, 1, 1),
        ButtonTile("", color, 2, 1),
        ButtonTile("", color, 1, 1),
        ButtonTile("", color, 1, 1)
    )

    fun getTiles(): List<Tile> {
        return initialTileList
    }

    fun getTileList(): List<Tile> {
        return tileList
    }

    //private val tileLiveData = MutableLiveData(initialTileList)

    //fun getTileList(): LiveData<List<Tile>> {
    //    return tileLiveData
    //}

    //fun addTile(tile: Tile) {
    //    val currentList = tileLiveData.value
    //    if (currentList == null) {
    //        tileLiveData.postValue(listOf(tile))
    //    } else {
    //        val updatedList = currentList.toMutableList()
    //        updatedList.add(0, tile)
    //        tileLiveData.postValue(updatedList)
    //    }
    //}

    //fun removeTile() {
    //    val currentList = tileLiveData.value
    //    if (currentList != null) {
    //        val updatedList = currentList.toMutableList()
    //        updatedList.removeAt(0)
    //        tileLiveData.postValue(updatedList)
    //    }
    //}

    //companion object {
    //    private var INSTANCE: TilesSource? = null
    //    fun getDataSource(resources: Resources): TilesSource {
    //        return synchronized(TilesSource::class) {
    //            val newInstance = INSTANCE ?: TilesSource()
    //            INSTANCE = newInstance
    //            newInstance
    //        }
    //    }
    //}
}