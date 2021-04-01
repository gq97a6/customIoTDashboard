package com.example.app.tiles

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.app.dashboard_activity.DashboardAdapter

abstract class Tile(
        val id: Long,
        val name: String,
        private val layout: Int) {

    lateinit var context: Context

    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.TileViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)

        return DashboardAdapter.TileViewHolder(view)
    }

    open fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
    }

    open fun getItemViewType(context: Context): Int {
        this.context = context
        return layout
    }

    fun areItemsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem == newItem
    }

    fun areContentsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem.id == newItem.id
    }
}