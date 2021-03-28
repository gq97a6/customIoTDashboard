package com.example.app.tiles

import android.graphics.drawable.Drawable

data class Tile(
    val id: Long,
    val name: String,
    val sizeX: Byte,
    val sizeY: Byte,
    val layout: Int
)