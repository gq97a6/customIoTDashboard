package com.netDashboard.tiles

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netDashboard.R
import com.netDashboard.dashboard_activity.DashboardAdapter
import com.netDashboard.getScreenWidth
import java.util.*


abstract class Tile(
        var name: String,
        var color: Int,
        var layout: Int,
        var x: Int,
        var y: Int) {

    val id: Long?

    var swapMode = false
    var swapReady = false

    var spanCount = 1
    lateinit var context: Context
    var holder: DashboardAdapter.TileViewHolder? = null

    init {
        id = Random().nextLong()
    }

    fun getItemViewType(context: Context, spanCount: Int, swapMode: Boolean): Int {
        this.context = context
        this.spanCount = spanCount
        this.swapMode = swapMode

        return layout
    }

    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.TileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        view.findViewById<RecyclerView>(R.id.recycler_view)

        val params = view.layoutParams
        params.height = ((getScreenWidth() - view.paddingLeft * 2) / spanCount) * y
        view.layoutParams = params

        return DashboardAdapter.TileViewHolder(view)
    }

    open fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        this.holder = holder
    }

    fun areItemsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem == newItem
    }

    fun areContentsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem.id == newItem.id
    }

    abstract fun swapMode(isEnabled: Boolean)
    abstract fun swapReady(isReady: Boolean)
    abstract fun setThemeColor(color: Int)
}