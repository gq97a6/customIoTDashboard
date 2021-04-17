package com.netDashboard.tiles.tiles.longButton

import com.netDashboard.R
import com.netDashboard.createToast
import com.netDashboard.dashboard_activity.DashboardAdapter
import com.netDashboard.tiles.Tile

class LongButtonTile(id: Long, name: String,  x: Int, y: Int):
        Tile(id, name, R.layout.long_button_tile, x, y) {

    override fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val view = holder.itemView

        view.setOnClickListener {
            val ratio = view.width.toDouble() / view.height.toDouble()
            createToast(context, "$ratio || ${view.height} || ${view.width}")
        }
    }
}