package com.netDashboard.tiles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.netDashboard.dashboard_activity.DashboardAdapter
import com.netDashboard.getScreenWidth


abstract class Tile(
        val id: Long,
        var name: String,
        private var layout: Int,
        var x: Int,
        private var y: Int) {

    var swapFlag = false
    var spanCount = 2
    lateinit var context: Context

    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.TileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        view.setOnClickListener {
            onClick()
        }

        return DashboardAdapter.TileViewHolder(view)
    }

    open fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        val view = holder.itemView
        val params = view.layoutParams

        params.height = ((getScreenWidth() - view.paddingLeft * 2) / spanCount) * y
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

    open fun onClick() {

    }
}