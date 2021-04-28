package com.netDashboard.tiles.tiles.button

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.Button
import com.netDashboard.R
import com.netDashboard.createToast
import com.netDashboard.dashboard_activity.DashboardAdapter
import com.netDashboard.tiles.Tile
import java.util.*

class ButtonTile(name: String, color: Int, x: Int, y: Int) :
    Tile(name, color, R.layout.button_tile, x, y) {

    override fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        setThemeColor(color)

        holder.itemView.findViewById<Button>(R.id.button).setOnClickListener {
            holder.itemView.callOnClick()

            if(!editMode()) {
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

                setThemeColor(color)
            }

            holder.itemView.findViewById<Button>(R.id.button).setOnLongClickListener() {
                if (editMode()) {
                    createToast(context, "open settings! ${holder.adapterPosition}")
                }

                return@setOnLongClickListener true
            }
        }
    }

    override fun setThemeColor(color: Int) {
        this.color = color

        holder?.itemView?.findViewById<Button>(R.id.button)?.backgroundTintList =
            ColorStateList.valueOf(
                color
            )

        holder?.itemView?.findViewById<Button>(R.id.button)?.setTextColor(getContrastColor(color))
    }
}