package com.netDashboard.tiles

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.netDashboard.createToast
import java.util.*

class Adapter(private val context: Context, private val spanCount: Int) :
    ListAdapter<Tile, Adapter.TileViewHolder>(TileDiffCallback) {

    var swapMode = false
    var removeMode = false

    lateinit var tiles: MutableList<Tile>
    lateinit var currentTile: Tile

    override fun submitList(list: MutableList<Tile>?) {
        super.submitList(list)
        tiles = list!!.toMutableList() //Do not remove .{...}
    }

    override fun getCurrentList(): MutableList<Tile> {
        return tiles
    }

    override fun getItemCount(): Int {
        return tiles.size
    }

    override fun getItemViewType(position: Int): Int {
        currentTile = tiles[position]
        return tiles[position].getItemViewType(context, spanCount) //Tile function
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        return currentTile.onCreateViewHolder(parent, viewType) //Tile function
    }

    class TileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        tiles[position].onBindViewHolder(holder, position) //Tile function

        holder.itemView.setOnClickListener {
            when {
                swapMode -> {
                    tiles[position].flag(!tiles[position].flag())

                    for ((i, t) in tiles.withIndex()) {

                        if (t.flag() && tiles[position].id != t.id) {
                            tiles[i].flag(false)
                            tiles[position].flag(false)

                            Collections.swap(tiles, position, i)

                            notifyItemChanged(position)
                            notifyItemChanged(i)
                        }
                    }
                }
                removeMode -> {
                    for ((i, t) in tiles.withIndex()) {

                        if (t.flag() && tiles[position].id != t.id) {
                            tiles[i].flag(false)
                        }
                    }

                    tiles[position].flag(!tiles[position].flag(), 1)
                }
                else -> {
                    createToast(context, "$position:S")
                }
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