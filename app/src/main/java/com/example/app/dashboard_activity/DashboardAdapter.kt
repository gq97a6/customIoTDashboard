package com.example.app.dashboard_activity

import com.example.app.tiles.Tile
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R

class DashboardAdapter(private val onClick: (Tile) -> Unit):
        ListAdapter<Tile, DashboardAdapter.TileViewHolder>(TileDiffCallback) {

    class TileViewHolder(itemView: View, val onClick: (Tile) -> Unit):
            RecyclerView.ViewHolder(itemView) {

        private var currentTile: Tile? = null
        //private val tileTextView: TextView = itemView.findViewById(R.id.Tile_text)
        //private val tileImageView: ImageView = itemView.findViewById(R.id.Tile_image)

        init {
            itemView.setOnClickListener {
                currentTile?.let {
                    onClick(it)
                }
            }
        }

        fun bind(Tile: Tile) {
            currentTile = Tile

            //TileTextView.text = Tile.name
            //if (Tile.image != null) {
            //    TileImageView.setImageResource(Tile.image)
            //} else {
            //  TileImageView.setImageResource(R.drawable.rose)
            //}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.slider_tile, parent, false) //TODO

        return TileViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        val tile = getItem(position)
        holder.bind(tile)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}

object TileDiffCallback : DiffUtil.ItemCallback<Tile>() {
    override fun areItemsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Tile, newItem: Tile): Boolean {
        return oldItem.id == newItem.id
    }
}