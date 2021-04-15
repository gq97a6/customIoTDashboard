package com.example.app.tiles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.app.R
import com.example.app.dashboard_activity.DashboardAdapter
import com.example.app.getScreenWidth

abstract class Tile(
        private val id: Long,
        val name: String,
        private val layout: Int) {

    lateinit var context: Context
    var spanCount: Int = 2

    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.TileViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)

        return DashboardAdapter.TileViewHolder(view)
    }

    open fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        val view = holder.itemView
        val params = view.layoutParams

        params.height = (getScreenWidth() - view.paddingLeft * 2) / spanCount
        view.layoutParams = params
    }

    open fun getItemViewType(context: Context, spanCount: Int): Int {
        this.context = context
        this.spanCount = spanCount

        return layout
    }

    fun areItemsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem == newItem
    }

    fun areContentsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem.id == newItem.id
    }
}