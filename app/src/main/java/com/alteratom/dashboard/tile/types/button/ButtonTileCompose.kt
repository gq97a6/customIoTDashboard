package com.alteratom.tile.types.button.compose

import androidx.compose.runtime.Composable
import com.alteratom.dashboard.activities.fragments.tile_properties.TilePropertiesCompose
import com.alteratom.dashboard.activities.fragments.tile_properties.TilePropertiesMqttCompose.Communication
import com.alteratom.dashboard.foreground_service.demons.DaemonBasedCompose

object ButtonTileCompose : DaemonBasedCompose {
    @Composable
    override fun Mqttd() {
        TilePropertiesCompose.Box {
            TilePropertiesCompose.CommunicationBox {
                Communication()
            }
            TilePropertiesCompose.Notification()
        }
    }

    @Composable
    override fun Bluetoothd() {
    }
}