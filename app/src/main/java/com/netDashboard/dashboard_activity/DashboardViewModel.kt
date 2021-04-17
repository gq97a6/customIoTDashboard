package com.netDashboard.dashboard_activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.netDashboard.tiles.TilesSource

class TilesListViewModel(val dataSource: TilesSource) : ViewModel() {
    val tilesLiveData = dataSource.getTileList()
}

class TilesListViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TilesListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TilesListViewModel(
                dataSource = TilesSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}