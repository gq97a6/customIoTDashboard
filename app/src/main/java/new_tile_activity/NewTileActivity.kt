package new_tile_activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.netDashboard.databinding.NewTileActivityBinding
import com.netDashboard.tiles.tiles.button.ButtonTile
import com.netDashboard.tiles.tiles.slider.SliderTile

class NewTileActivity : AppCompatActivity() {
    lateinit var b: NewTileActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = NewTileActivityBinding.inflate(layoutInflater)
        setContentView(b.root)

        val color = Color.parseColor("#00000000")

        val initialTileList = listOf(
            ButtonTile("", color, 3, 1),
            SliderTile("", color, 3, 1),
        )
    }
}