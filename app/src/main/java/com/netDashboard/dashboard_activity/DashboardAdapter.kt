package com.netDashboard.dashboard_activity

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.netDashboard.createToast
import com.netDashboard.tiles.Tile
import java.util.*

class DashboardAdapter(private val context: Context, private val spanCount: Int) :
    ListAdapter<Tile, DashboardAdapter.TileViewHolder>(TileDiffCallback) {

    var swapMode = false
    lateinit var tiles: MutableList<Tile>
    lateinit var currentTile: Tile

    override fun submitList(list: MutableList<Tile>?) {
        super.submitList(list)
        tiles = list!!
    }

    override fun getItemViewType(position: Int): Int {
        currentTile = tiles[position]
        return tiles[position].getItemViewType(context, spanCount, swapMode) //Tile function
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        return currentTile.onCreateViewHolder(parent, viewType) //Tile function
    }

    class TileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        tiles[position].onBindViewHolder(holder, position) //Tile function

        holder.itemView.setOnClickListener {
            if (swapMode) {
                tiles[position].swapReady(!tiles[position].swapReady)

                for ((i, t) in tiles.withIndex()) {

                    if (t.swapReady && tiles[position].id != t.id) {
                        tiles[i].swapReady(false)
                        tiles[position].swapReady(false)

                        Collections.swap(tiles, position, i)

                        notifyItemChanged(position)
                        notifyItemChanged(i)
                    }
                }
            } else {
                createToast(context, position.toString())
            }
        }
    }
}

object TileDiffCallback : DiffUtil.ItemCallback<Tile>() {
    override fun areItemsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem.areItemsTheSame(oldItem, newItem) //Tile function
    }

    override fun areContentsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem.areContentsTheSame(oldItem, newItem) //Tile function
    }
}