package com.alteratom.tile.types.color.compose

import androidx.compose.runtime.Composable
import com.alteratom.dashboard.compose_daemon.TilePropertiesCompose
import com.alteratom.dashboard.compose_daemon.TilePropertiesMqttCompose.Communication
import com.alteratom.dashboard.compose_daemon.DaemonBasedCompose

object GraphTileCompose : DaemonBasedCompose {
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