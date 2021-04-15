import com.example.app.R
import com.example.app.createToast
import com.example.app.dashboard_activity.DashboardAdapter
import com.example.app.tiles.Tile

class DiodeTile(id: Long, name: String, val x: Int, val y: Int):
        Tile(id, name, R.layout.diode_tile) {

    override fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val view = holder.itemView

        view.setOnClickListener {
            val ratio = view.width.toDouble() / view.height.toDouble()
            createToast(context, "$ratio || ${view.height} || ${view.width}")
        }
    }
}