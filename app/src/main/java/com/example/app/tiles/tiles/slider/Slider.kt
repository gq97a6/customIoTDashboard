import android.content.Context
import android.view.ViewGroup
import com.example.app.R
import com.example.app.dashboard_activity.DashboardAdapter
import com.example.app.tiles.Tile

class SliderTile(id: Long, name: String, val x: Int, val y: Int):
        Tile(id, name, R.layout.slider_tile) {

    override fun getItemViewType(context: Context): Int {
        return super.getItemViewType(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.TileViewHolder {
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
}