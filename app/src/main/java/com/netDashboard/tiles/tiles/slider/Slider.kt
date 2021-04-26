package com.netDashboard.tiles.tiles.slider

import com.google.android.material.slider.Slider
import com.netDashboard.R
import com.netDashboard.createToast
import com.netDashboard.dashboard_activity.DashboardAdapter
import com.netDashboard.tiles.Tile
import java.util.*

class SliderTile(name: String, color: Int, x: Int, y: Int) :
    Tile(name, color, R.layout.slider_tile, x, y) {

    override fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.findViewById<Slider>(R.id.slider).isEnabled = !swapMode

        setThemeColor(color)
    }

    override fun swapMode(isEnabled: Boolean) {
        super.swapMode(isEnabled)

        holder?.itemView?.findViewById<Slider>(R.id.slider)?.isEnabled = !swapMode
    }
}