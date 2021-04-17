package com.netDashboard.tiles

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.netDashboard.dashboard_activity.DashboardAdapter
import com.netDashboard.getScreenWidth


abstract class Tile(
    private val id: Long,
    val name: String,
    private val layout: Int,
    val span: Int = 1
) {

    lateinit var context: Context
    var spanCount: Int = 2

    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.TileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return DashboardAdapter.TileViewHolder(view)
    }

    open fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        val view = holder.itemView
        val params = view.layoutParams

        params.height = (getScreenWidth() - view.paddingLeft * 2) / spanCount
        //params.width = params.height * 2
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