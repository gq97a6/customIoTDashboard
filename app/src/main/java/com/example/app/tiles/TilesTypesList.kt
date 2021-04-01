package com.example.app.tiles

import ButtonTile
import SliderTile

fun tilesTypesList(): List<Tile> {
    return listOf(
        ButtonTile(0, "", 1, 1),
        SliderTile(1, "", 1, 1)
    )
}