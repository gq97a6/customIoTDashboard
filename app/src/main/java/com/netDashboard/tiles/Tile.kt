package com.netDashboard.tiles

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.netDashboard.R
import com.netDashboard.createToast
import com.netDashboard.dashboard_activity.DashboardActivity
import com.netDashboard.dashboard_activity.DashboardAdapter
import com.netDashboard.getScreenWidth


abstract class Tile(
        val id: Long,
        var name: String,
        var layout: Int,
        var x: Int,
        private var y: Int) {

    var swapFlag = false
    var spanCount = 2
    lateinit var context: Context
    lateinit var holder: DashboardAdapter.TileViewHolder

    fun getItemViewType(context: Context, spanCount: Int): Int {
        this.context = context
        this.spanCount = spanCount

        return layout
    }

    open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.TileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        view.findViewById<RecyclerView>(R.id.recycler_view)

        val params = view.layoutParams
        params.height = ((getScreenWidth() - view.paddingLeft * 2) / spanCount) * y
        view.layoutParams = params

        holder = DashboardAdapter.TileViewHolder(view)

        return holder
    }

    open fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
    }

    fun areItemsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem == newItem
    }

    fun areContentsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem.id == newItem.id
    }

    open fun viewOnClick() {
        createToast(context, "viewOnClick")
    }
}