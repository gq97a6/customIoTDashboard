package com.alteratom.tile.types.color.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alteratom.dashboard.BasicButton
import com.alteratom.dashboard.EditText
import com.alteratom.dashboard.G.tile
import com.alteratom.dashboard.activities.MainActivity.Companion.fm
import com.alteratom.dashboard.activities.fragments.*
import com.alteratom.dashboard.activities.fragments.tile_properties.TilePropertiesCompose
import com.alteratom.dashboard.activities.fragments.tile_properties.TilePropertiesMqttCompose.Communication
import com.alteratom.dashboard.foreground_service.demons.DaemonBasedCompose
import com.alteratom.tile.types.switch.SwitchTile

object SwitchTileCompose : DaemonBasedCompose {
    @Composable
    override fun Mqttd() {
        val tile = tile as SwitchTile
        TilePropertiesCompose.Box {
            TilePropertiesCompose.CommunicationBox {
                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    BasicButton(
                        onClick = {
                            getIconHSV = { tile.hsvFalse }
                            getIconRes = { tile.iconResFalse }
                            getIconColorPallet = { tile.palletFalse }

                            setIconHSV = { hsv -> tile.hsvFalse = hsv }
                            setIconKey = { key -> tile.iconKeyFalse = key }

                            fm.replaceWith(TileIconFragment())
                        },
                        border = BorderStroke(0.dp, tile.palletFalse.cc.color),
                        modifier = Modifier
                            .height(52.dp)
                            .width(52.dp)
                    ) {
                        Icon(
                            painterResource(tile.iconResFalse),
                            "",
                            tint = tile.palletFalse.cc.color
                        )
                    }

                    var off by remember { mutableStateOf(tile.mqttData.payloads["false"] ?: "") }
                    EditText(
                        label = { Text("Off payload") },
                        value = off,
                        onValueChange = {
                            off = it
                            tile.mqttData.payloads["false"] = it
                        },
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    BasicButton(
                        onClick = {
                            getIconHSV = { tile.hsvTrue }
                            getIconRes = { tile.iconResTrue }
                            getIconColorPallet = { tile.palletTrue }

                            setIconHSV = { hsv -> tile.hsvTrue = hsv }
                            setIconKey = { key -> tile.iconKeyTrue = key }

                            fm.replaceWith(TileIconFragment())
                        },
                        border = BorderStroke(0.dp, tile.palletTrue.cc.color),
                        modifier = Modifier
                            .height(52.dp)
                            .width(52.dp)
                    ) {
                        Icon(painterResource(tile.iconResTrue), "", tint = tile.palletTrue.cc.color)
                    }

                    var on by remember { mutableStateOf(tile.mqttData.payloads["true"] ?: "") }
                    EditText(
                        label = { Text("On payload") },
                        value = on,
                        onValueChange = {
                            on = it
                            tile.mqttData.payloads["true"] = it
                        },
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Communication()
            }
            TilePropertiesCompose.Notification()
        }
    }

    @Composable
    override fun Bluetoothd() {
    }
}