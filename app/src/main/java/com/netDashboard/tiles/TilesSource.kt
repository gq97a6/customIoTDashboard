package com.netDashboard.tiles

import android.graphics.Color
import com.netDashboard.getRandomColor
import com.netDashboard.tiles.tiles_types.button.ButtonTile
import com.netDashboard.tiles.tiles_types.slider.SliderTile
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class TilesSource {
    val color = Color.parseColor("#00000000")

    private val tileList = listOf(
        ButtonTile("", color, 3, 1),
        SliderTile("", color, 3, 1)
    )

    private val initialTileList = listOf(
        ButtonTile("", getRandomColor(), 1, 1),
        SliderTile("", getRandomColor(), 1, 1),
        ButtonTile("", getRandomColor(), 3, 2),
        ButtonTile("", getRandomColor(), 2, 1),
        ButtonTile("", getRandomColor(), 1, 1),
        ButtonTile("", getRandomColor(), 1, 1),
        SliderTile("", getRandomColor(), 1, 1),
        ButtonTile("", getRandomColor(), 3, 1),
        SliderTile("", getRandomColor(), 1, 1),
        ButtonTile("", getRandomColor(), 2, 1),
        SliderTile("", getRandomColor(), 1, 1),
        ButtonTile("", getRandomColor(), 1, 1),
        SliderTile("", getRandomColor(), 1, 1),
        ButtonTile("", getRandomColor(), 2, 1),
        ButtonTile("", getRandomColor(), 1, 1),
        ButtonTile("", getRandomColor(), 1, 1),
        SliderTile("", getRandomColor(), 2, 1),
        ButtonTile("", getRandomColor(), 2, 1),
        ButtonTile("", getRandomColor(), 3, 1),
        SliderTile("", getRandomColor(), 1, 1),
        ButtonTile("", getRandomColor(), 2, 1),
        ButtonTile("", getRandomColor(), 1, 1),
        ButtonTile("", getRandomColor(), 1, 1)
    )

    fun getTiles(): List<Tile> {
        return initialTileList
    }

    fun getTileList(): List<Tile> {
        val tileList = this.tileList

        //Enable edit mode
        for ((i, _) in tileList.withIndex()) {
            tileList[i].editMode(true)
        }

        return tileList
    }

    fun saveExample(list: List<Tile>, name: String) {
        val file = FileOutputStream(name)

        val outStream = ObjectOutputStream(file)

        outStream.writeObject(list)

        outStream.close()
        file.close()
    }

    fun getExample(name: String): List<Tile>? {
        val file = FileInputStream(name)
        val inStream = ObjectInputStream(file)

        val list = inStream.readObject() as List<*>

        inStream.close()
        file.close()

        return list.filterIsInstance<Tile>().takeIf { it.size == list.size }
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