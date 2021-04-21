package com.netDashboard.dashboard_activity

import android.content.Context
import android.util.Log
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
        Log.i("OUY", "submitList")
        tiles = list!!
    }

    override fun getItemViewType(position: Int): Int {
        Log.i("OUY", "getItemViewType")
        currentTile = tiles[position]
        return tiles[position].getItemViewType(context, spanCount) //Tile function
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        Log.i("OUY", "onCreateViewHolder")
        return currentTile.onCreateViewHolder(parent, viewType) //Tile function
    }

    class TileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        Log.i("OUY", "onBindViewHolder")
        tiles[position].onBindViewHolder(holder, position) //Tile function

        holder.itemView.setOnClickListener {
            if (swapMode) {
                tiles[position].swapFlag = !tiles[position].swapFlag

                for ((i, t) in tiles.withIndex()) {

                    if (t.swapFlag && tiles[position].id != t.id) {
                        tiles[i].swapFlag = false
                        tiles[position].swapFlag = false

                        Log.i("OUY", "$position, $i")

                        Collections.swap(tiles, position, i)
                        notifyItemChanged(position)
                        notifyItemChanged(i)
                    }
                }
            }
            else
            {
                createToast(context, "$position")
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