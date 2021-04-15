import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.app.R
import com.example.app.createToast
import com.example.app.dashboard_activity.DashboardAdapter
import com.example.app.getScreenWidth
import com.example.app.tiles.Tile

class ButtonTile(id: Long, name: String, val x: Int, val y: Int):
        Tile(id, name, R.layout.button_tile) {

    override fun onBindViewHolder(holder: DashboardAdapter.TileViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val view = holder.itemView

        view.setOnClickListener {
            val params = view.layoutParams

            //view.setBackgroundColor(Color.parseColor("#FFFFFF"))

            params.height = (getScreenWidth() - view.paddingLeft * 2) / spanCount
            view.layoutParams = params

            val ratio = view.width.toDouble() / view.height.toDouble()
            createToast(context, "$ratio || ${view.height} || ${view.width}")
        }
    }
}