package com.netDashboard.tiles

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.ColorUtils
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

    private var editMode = false
    private var flag = false

    var spanCount = 1
    lateinit var context: Context
    var holder: DashboardAdapter.TileViewHolder? = null

    init {
        id = Random().nextLong()
    }

    fun getItemViewType(context: Context, spanCount: Int): Int {
        this.context = context
        this.spanCount = spanCount

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
            if (editMode) {
                createToast(context, "open settings! ${holder.adapterPosition}")
            }

            return@setOnLongClickListener true
        }

        editMode(editMode)
        flag(flag)
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

    open fun editMode(isEnabled: Boolean) {
        editMode = isEnabled
    }

    fun editMode(): Boolean {
        return editMode
    }

    open fun flag(flag: Boolean, type: Int = 0) {
        this.flag = flag

        val flagMark = holder?.itemView?.findViewById<ImageView>(R.id.swapReady)

        when(type) {
            0 -> flagMark?.setBackgroundResource(R.drawable.icon_swap)
            1 -> flagMark?.setBackgroundResource(R.drawable.icon_remove)
        }

        if (flag) {
            flagMark?.backgroundTintList = ColorStateList.valueOf(getContrastColor(color))
            flagMark?.visibility = View.VISIBLE
        } else {
            flagMark?.visibility = View.GONE
        }
    }

    fun flag(): Boolean {
        return flag
    }

    open fun setThemeColor(color: Int) {}
}