package com.netDashboard.tiles.tiles.button

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button
import com.netDashboard.R
import com.netDashboard.dashboard_activity.DashboardAdapter
import com.netDashboard.tiles.Tile
import java.util.*

class ButtonTile(id: Long, name: String, x: Int, y: Int):
        Tile(id, name, R.layout.button_tile, x, y) {

    override fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        holder.itemView.findViewById<Button>(R.id.test2).setOnClickListener {
            holder.itemView.callOnClick()

            //val rnd = Random()
            //val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            ////Color.parseColor("#A3B500")
            //it.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}