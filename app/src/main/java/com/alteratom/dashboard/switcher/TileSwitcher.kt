package com.alteratom.dashboard.switcher

import android.view.MotionEvent
import androidx.compose.ui.input.pointer.PointerEvent
import com.alteratom.dashboard.G.dashboard
import com.alteratom.dashboard.G.tile
import com.alteratom.dashboard.activities.MainActivity.Companion.fm
import com.alteratom.dashboard.activities.fragments.tile_properties.TilePropertiesFragment
import com.alteratom.dashboard.activities.MainActivity.FragmentManager.Animations.slideLeft as slideLeftAnimation
import com.alteratom.dashboard.activities.MainActivity.FragmentManager.Animations.slideRight as slideRightAnimation

object TileSwitcher : Switcher() {

    fun switch(slideRight: Boolean) {
        if (dashboard.tiles.size < 2) return

        var index = dashboard.tiles.indexOf(tile) - if (slideRight) 1 else -1
        if (index < 0) index = dashboard.tiles.lastIndex
        if (index > dashboard.tiles.lastIndex) index = 0

        tile = dashboard.tiles[index]

        fm.replaceWith(
            TilePropertiesFragment(),
            false,
            if (slideRight) slideRightAnimation else slideLeftAnimation
        )
    }

    fun handle(e: MotionEvent?) = handle(e, { r -> switch(r) })
    fun handle(e: PointerEvent) = handle(e, { r -> switch(r) })
}