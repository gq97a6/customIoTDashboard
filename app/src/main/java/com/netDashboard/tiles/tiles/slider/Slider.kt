package com.netDashboard.tiles.tiles.slider

import android.view.View
import android.widget.ImageView
import com.google.android.material.slider.Slider
import com.netDashboard.R
import com.netDashboard.dashboard_activity.DashboardAdapter
import com.netDashboard.tiles.Tile

class SliderTile(name: String, color: Int, x: Int, y: Int) :
    Tile(name, color, R.layout.slider_tile, x, y) {

    override fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.findViewById<Slider>(R.id.slider).isEnabled = !swapMode
        setThemeColor(color)
    }

    override fun swapMode(isEnabled: Boolean) {
        swapMode = isEnabled

        holder?.itemView?.findViewById<Slider>(R.id.slider)?.isEnabled = !swapMode
    }

    override fun swapReady(isReady: Boolean) {
        swapReady = isReady

        if (swapReady) {
            holder?.itemView?.findViewById<ImageView>(R.id.swapReady)?.visibility = View.VISIBLE
        } else {
            holder?.itemView?.findViewById<ImageView>(R.id.swapReady)?.visibility = View.GONE
        }
    }

    override fun setThemeColor(color: Int) {
    }
}