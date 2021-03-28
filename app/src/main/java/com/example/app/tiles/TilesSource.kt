/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.app.tiles

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// Handles operations on tileLiveData and holds details about it.
class TilesSource(resources: Resources) {
    private val initialTileList = listOf(tilesTypesList()[0], tilesTypesList()[1], tilesTypesList()[2] , tilesTypesList()[3])
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

    fun getTileById(id: Long): Tile? {
        tileLiveData.value?.let { tiles ->
            return tiles.firstOrNull{ it.id == id}
        }
        return null
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