package com.netDashboard.tiles.tiles.button

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import com.netDashboard.R
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

            if(!swapMode) {
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

                setThemeColor(color)
            }
        }
    }

    override fun swapMode(isEnabled: Boolean) {
        swapMode = isEnabled
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
        this.color = color

        val textColor = if (ColorUtils.calculateLuminance(color) < 0.5) {
            Color.parseColor("#FFFFFFFF")
        } else {
            Color.parseColor("#000000")
        }

        holder?.itemView?.findViewById<Button>(R.id.button)?.backgroundTintList =
            ColorStateList.valueOf(
                color
            )

        holder?.itemView?.findViewById<Button>(R.id.button)?.setTextColor(textColor)
    }
}