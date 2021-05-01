package com.netDashboard.config_new_tile_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.netDashboard.databinding.ConfigNewTileActivityBinding

class ConfigNewTileActivity : AppCompatActivity() {
    lateinit var b: ConfigNewTileActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ConfigNewTileActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
    }
}