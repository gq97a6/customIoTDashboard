package com.netDashboard.tiles

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.netDashboard.R
import com.netDashboard.createToast
import com.netDashboard.dashboard_activity.DashboardAdapter
import com.netDashboard.getScreenWidth
import java.util.*


abstract class Tile(
    var name: String,
    var color: Int,
    var layout: Int,
    var x: Int,
    var y: Int
) {

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

        return DashboardAdapter.TileViewHolder(view)
    }

    open fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        this.holder = holder

        val view = holder.itemView
        val params = view.layoutParams
        params.height = ((getScreenWidth() - view.paddingLeft * 2) / spanCount) * y
        view.layoutParams = params

        holder.itemView.setOnLongClickListener() {
            if (swapMode) {
                createToast(context, "open settings! ${holder.adapterPosition}")
            }

            return@setOnLongClickListener true
        }
    }

    fun areItemsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem == newItem
    }

    fun areContentsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem.id == newItem.id
    }

    fun getContrastColor(themeColor: Int): Int {

        return if (ColorUtils.calculateLuminance(themeColor) < 0.5) {
            Color.parseColor("#FFFFFFFF")
        } else {
            -16777216
        }
    }

    open fun swapMode(isEnabled: Boolean) {
        swapMode = isEnabled
    }

    open fun swapReady(isReady: Boolean) {
        swapReady = isReady

        val swapReadyMark = holder?.itemView?.findViewById<ImageView>(R.id.swapReady)

        if (swapReady) {
            swapReadyMark?.backgroundTintList = ColorStateList.valueOf(getContrastColor(color))
            swapReadyMark?.visibility = View.VISIBLE
        } else {
            swapReadyMark?.visibility = View.GONE
        }
    }

    open fun setThemeColor(color: Int) {}
}