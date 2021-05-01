package com.netDashboard.new_tile_activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewTileViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewTileViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class.")
    }
}