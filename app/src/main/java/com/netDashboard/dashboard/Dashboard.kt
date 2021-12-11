package com.netDashboard.dashboard

import android.widget.Button
import com.fasterxml.jackson.annotation.JsonIgnore
import com.netDashboard.R
import com.netDashboard.foreground_service.DaemonGroup
import com.netDashboard.recycler_view.BaseRecyclerViewAdapter
import com.netDashboard.recycler_view.BaseRecyclerViewItem
import com.netDashboard.tile.Tile
import java.util.*
import kotlin.random.Random

@Suppress("UNUSED")
class Dashboard(var name: String = "", var isInvalid: Boolean = false) : BaseRecyclerViewItem() {

    override val layout
        get() = R.layout.item_dashboard

    @JsonIgnore
    var daemonGroup: DaemonGroup? = null

    @JsonIgnore
    var tilesAdapterEditMode: BaseRecyclerViewAdapter<Tile>.Modes? = null
    var tiles: MutableList<Tile> = mutableListOf()
        set(value) {
            for (t in value) t.dashboard = this
            field = value
        }

    var mqttEnabled = true

    var mqttAddress = "tcp://"
    var mqttPort = 1883
    var mqttUserName: String? = ""
        get() = if ((field ?: "").isBlank()) null else field
    var mqttPass: String? = ""
        get() = if ((field ?: "").isBlank()) null else field
    var mqttClientId: String = kotlin.math.abs(Random.nextInt()).toString()
    val mqttURI
        get() = "$mqttAddress:$mqttPort"

    var bluetoothEnabled = false

    companion object {
        fun MutableList<Dashboard>.byId(id: Long): Dashboard =
            this.find { it.id == id } ?: Dashboard(isInvalid = true)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewAdapter.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.findViewById<Button>(R.id.id_button).text =
            name.uppercase(Locale.getDefault())

        holder.itemView.findViewById<Button>(R.id.id_button).setOnClickListener {
            holder.itemView.callOnClick()
        }
    }
}