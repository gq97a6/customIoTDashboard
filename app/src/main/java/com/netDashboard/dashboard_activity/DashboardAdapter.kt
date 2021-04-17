package com.netDashboard.dashboard_activity

import android.content.Context
import com.netDashboard.tiles.Tile
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class DashboardAdapter(val context: Context, private val spanCount: Int): ListAdapter<Tile, DashboardAdapter.TileViewHolder>(
    TileDiffCallback
) {

    lateinit var tiles: MutableList<Tile>
    lateinit var currentTile: Tile

    override fun submitList(list: MutableList<Tile>?) {
        super.submitList(list)

        tiles = list!!
    }

    override fun getItemViewType(position: Int): Int {
        currentTile = tiles[position]
        return currentTile.getItemViewType(context, spanCount) //Tile function
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        return currentTile.onCreateViewHolder(parent, viewType) //Tile function
    }

    class TileViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        currentTile.onBindViewHolder(holder, position) //Tile function
    }
}

object TileDiffCallback: DiffUtil.ItemCallback<Tile>() {
    override fun areItemsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem.areItemsTheSame(oldItem, newItem) //Tile function
    }

    override fun areContentsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem.areContentsTheSame(oldItem, newItem) //Tile function
    }
}