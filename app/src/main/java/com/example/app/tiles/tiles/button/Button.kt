import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.app.R
import com.example.app.createToast
import com.example.app.dashboard_activity.DashboardAdapter
import com.example.app.tiles.Tile

class ButtonTile(id: Long, name: String, val x: Int, val y: Int):
        Tile(id, name, R.layout.button_tile) {

    override fun getItemViewType(context: Context): Int {
        return super.getItemViewType(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardAdapter.TileViewHolder {
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val button: Button = holder.itemView.findViewById(R.id.button)
        button.setOnClickListener() {
            createToast(context, "test")
        }
    }
}