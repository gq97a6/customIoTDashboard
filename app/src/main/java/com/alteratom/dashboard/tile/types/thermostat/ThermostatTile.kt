package com.alteratom.tile.types.thermostat

import android.app.Dialog
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import com.alteratom.R
import com.alteratom.dashboard.DialogBuilder.dialogSetup
import com.alteratom.dashboard.Theme
import com.alteratom.dashboard.compose.ComposeTheme
import com.alteratom.dashboard.recycler_view.RecyclerViewAdapter
import com.alteratom.dashboard.recycler_view.RecyclerViewItem
import com.alteratom.dashboard.round
import com.arctextview.ArcTextView
import com.fasterxml.jackson.annotation.JsonIgnore
import me.tankery.lib.circularseekbar.CircularSeekBar
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.math.abs

class ThermostatTile : com.alteratom.dashboard.tile.Tile() {

    @JsonIgnore
    override val layout = R.layout.tile_thermostat

    @JsonIgnore
    override var typeTag = "thermostat"

    override var iconKey = "il_weather_temperature_half"

    @JsonIgnore
    override var height = 2f

    private var hasReceived = MutableLiveData("")

    var mode: String? = null
    private var temp: Float? = null
        set(value) {
            field = value
            holder?.itemView?.findViewById<TextView>(R.id.th_temp_value)?.text = value.let {
                "${it?.round(3) ?: "--"} °C"
            }
            value?.let {
                holder?.itemView?.findViewById<CircularSeekBar>(R.id.th_temp)?.progress =
                    (it - temperatureRange[0]) / temperatureStep
            }
        }

    private var humi: Float? = null
        set(value) {
            field = value
            holder?.itemView?.findViewById<TextView>(R.id.th_humi_value)?.text = value.let {
                "${it?.round(3) ?: "--"} %"
            }
            value?.let {
                holder?.itemView?.findViewById<CircularSeekBar>(R.id.th_humi)?.progress =
                    it / humidityStep
            }
        }

    private var tempSetpoint: Float? = null
        set(value) {
            field = value
            value?.let {
                holder?.itemView?.findViewById<CircularSeekBar>(R.id.th_temp_setpoint)?.progress =
                    (it - temperatureRange[0]) / temperatureStep
            }
        }

    private var humiSetpoint: Float? = null
        set(value) {
            field = value
            value?.let {
                holder?.itemView?.findViewById<CircularSeekBar>(R.id.th_humi_setpoint)?.progress =
                    it / humidityStep
            }
        }

    var humidityStep = 5f
    var temperatureStep = 0.5f
    var temperatureRange = mutableListOf(15, 30)
    val modes = mutableListOf("Auto" to "0", "Heat" to "1", "Cool" to "2", "Off" to "3")
    val retain = mutableListOf(false, false, false) //temp, humi, mode

    var includeHumiditySetpoint = false
    var showPayload = false

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        abs(100 / humidityStep).let {
            holder.itemView.findViewById<CircularSeekBar>(R.id.th_humi).max = it
            holder.itemView.findViewById<CircularSeekBar>(R.id.th_humi_setpoint).max = it
        }
        abs((temperatureRange[1] - temperatureRange[0]) / temperatureStep).let {
            holder.itemView.findViewById<CircularSeekBar>(R.id.th_temp).max = it
            holder.itemView.findViewById<CircularSeekBar>(R.id.th_temp_setpoint).max = it
        }

        temp = temp
        tempSetpoint = tempSetpoint

        humi = humi
        humiSetpoint = humiSetpoint
    }

    override fun onClick(v: View, e: MotionEvent) {
        super.onClick(v, e)

        if (temperatureRange[0] > temperatureRange[1]) {
            val tmp = temperatureRange[0]
            temperatureRange[0] = temperatureRange[1]
            temperatureRange[1] = tmp
        }
        tempSetpoint?.let {
            if (it < temperatureRange[0] || it > temperatureRange[1]) {
                tempSetpoint = temperatureRange[0].toFloat()
            }
        }

        val dialog = Dialog(adapter.context)
        var modeAdapter = RecyclerViewAdapter<RecyclerViewItem>(adapter.context)

        dialog.setContentView(R.layout.dialog_thermostat)
        dialog.setContentView(ComposeView(adapter.context).apply {
            setContent {

                ComposeTheme(Theme.isDark) {
                    Box {
                        val selectedItem = remember { mutableStateOf(0) }

                        // Adds view to Compose
                        AndroidView(
                            modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
                            factory = { context ->
                                // Creates custom view
                                ArcTextView(context).apply {
                                }
                            },
                            update = { view ->
                                // View's been inflated or state read in this block has been updated
                                // Add logic here if necessary

                                // As selectedItem is read here, AndroidView will recompose
                                // whenever the state changes
                                // Example of Compose -> View communication
                                // view.
                                // view.coordinator.selectedItem = selectedItem.value
                            }
                        )
                    }
                }
            }
        })
/*
        val binding = DialogThermostatBinding.bind(dialog.findViewById(R.id.root))

        val observer: (String) -> Unit = { it ->
            when (it) {
                "temp" -> temp?.let {
                    binding.dtTempCurrent.text = "${it.round(3)}°C"
                }
                "humi" -> humi?.let {
                    binding.dtHumiCurrent.text = "${it.round(3)}%"
                }
                "temp_set" -> tempSetpoint?.let {
                    binding.dtTemp.progress = (it - temperatureRange[0]) / temperatureStep
                }
                "humi_set" -> if (includeHumiditySetpoint) {
                    humiSetpoint?.let {
                        binding.dtHumi.progress = it / humidityStep
                    }
                }
                "mode" -> modeAdapter.notifyDataSetChanged()
            }
        }

        dialog.setOnDismissListener {
            hasReceived.removeObserver(observer)
        }

        hasReceived.observe(adapter.context as LifecycleOwner, observer)

        var tempSetpointTmp = tempSetpoint
        var humiSetpointTmp = humiSetpoint

        fun valueBlinking() {
            if (tempSetpointTmp != tempSetpoint) binding.dtTempSetpoint.blink(-1, 200, 0, .4f)
            else binding.dtTempSetpoint.clearAnimation()

            if (humiSetpointTmp != humiSetpoint) binding.dtHumiSetpoint.blink(-1, 200, 0, .4f)
            else binding.dtHumiSetpoint.clearAnimation()
        }

        binding.dtTemp.setOnSeekBarChangeListener(object :
            CircularSeekBar.OnCircularSeekBarChangeListener {
            override fun onProgressChanged(
                circularSeekBar: CircularSeekBar?,
                progress: Float,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    tempSetpointTmp =
                        temperatureRange[0] + progress.roundToInt() * temperatureStep
                    binding.dtValue.text = "${tempSetpointTmp!!.round(3)}°C"
                    binding.dtTempSetpoint.text = "${tempSetpointTmp!!.round(3)}°C"
                    valueBlinking()
                }
            }

            override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {}
            override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {}
        })

        binding.dtHumi.setOnSeekBarChangeListener(object :
            CircularSeekBar.OnCircularSeekBarChangeListener {
            override fun onProgressChanged(
                circularSeekBar: CircularSeekBar?,
                progress: Float,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    humiSetpointTmp = progress.roundToInt() * humidityStep
                    binding.dtValue.text = "${humiSetpointTmp!!.round(3)}%"
                    binding.dtHumiSetpoint.text = "${humiSetpointTmp!!.round(3)}%"
                    valueBlinking()
                }
            }

            override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {}
            override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {}
        })

        binding.dtConfirm.setOnClickListener {
            fun send() {
                send("$tempSetpointTmp", mqtt.pubs["temp_set"], mqtt.qos, retain[0], true)
                send("$humiSetpointTmp", mqtt.pubs["humi_set"], mqtt.qos, retain[1], true)
            }

            if (mqtt.confirmPub) {
                with(adapter.context) {
                    buildConfirm("Confirm publishing", "PUBLISH", {
                        send()
                    })
                }
            } else send()

            dialog.dismiss()
        }

        binding.dtDeny.setOnClickListener {
            dialog.dismiss()
        }

        binding.dtMode.setOnClickListener {
            val notEmpty = modes.filter { !(it.first.isEmpty() && it.second.isEmpty()) }
            if (notEmpty.isNotEmpty() && !mqtt.pubs["mode"].isNullOrEmpty()) {

                val dialog = Dialog(adapter.context)
                modeAdapter = RecyclerViewAdapter(adapter.context)

                dialog.setContentView(R.layout.dialog_select)
                val binding = DialogSelectBinding.bind(dialog.findViewById(R.id.root))

                modeAdapter.onBindViewHolder = { _, holder, pos ->
                    val text = holder.itemView.findViewById<TextView>(R.id.is_text)
                    text.text = if (showPayload) "${notEmpty[pos].first} (${notEmpty[pos].second})"
                    else notEmpty[pos].first

                    holder.itemView.findViewById<View>(R.id.is_background).let {
                        it.backgroundTintList =
                            ColorStateList.valueOf(theme.a.pallet.color)
                        it.alpha = if (mode == notEmpty[pos].second) 0.15f else 0f
                    }
                }

                modeAdapter.onItemClick = {
                    val pos = modeAdapter.list.indexOf(it)

                    send(
                        this.modes[pos].second,
                        mqtt.pubs["mode"],
                        mqtt.qos,
                        retain[2]
                    )

                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, 50)
                }

                modeAdapter.setHasStableIds(true)
                modeAdapter.submitList(MutableList(notEmpty.size) {
                    RecyclerViewItem(
                        R.layout.item_select
                    )
                })

                binding.dsRecyclerView.layoutManager = LinearLayoutManager(adapter.context)
                binding.dsRecyclerView.adapter = modeAdapter

                dialog.dialogSetup()
                theme.apply(binding.root)
                dialog.show()
            } else createToast(modeAdapter.context, "Check setup")
        }

        if (!includeHumiditySetpoint) {
            binding.dtHumiCurrent.text = "--%"
            binding.dtHumi.visibility = INVISIBLE
        } else {
            abs(100 / humidityStep).let {
                binding.dtHumi.max = it
            }
            humiSetpointTmp?.let {
                binding.dtHumi.progress = it / humidityStep
            }

            humi?.let { binding.dtHumiCurrent.text = "${it.round(3)}%" }
            humiSetpointTmp?.let { binding.dtHumiSetpoint.text = "${it.round(3)}%" }
        }

        abs((temperatureRange[1] - temperatureRange[0]) / temperatureStep).let {
            binding.dtTemp.max = it
        }
        tempSetpointTmp?.let {
            binding.dtTemp.progress = (it - temperatureRange[0]) / temperatureStep
        }

        tempSetpointTmp?.let { binding.dtValue.text = "${it.round(3)}°C" }
        tempSetpointTmp?.let { binding.dtTempSetpoint.text = "${it.round(3)}°C" }
        temp?.let { binding.dtTempCurrent.text = "${it.round(3)}°C" }
*/
        dialog.dialogSetup()
        //theme.apply(binding.root, anim = false)
        dialog.show()
    }

    override fun onReceive(
        data: Pair<String?, MqttMessage?>,
        jsonResult: MutableMap<String, String>
    ) {
        super.onReceive(data, jsonResult)

        fun parse(value: String, field: String?) {
            var hasReceived = true

            val v = value.toFloatOrNull() ?: return
            when (field) {
                "temp" -> temp = v
                "temp_set" -> tempSetpoint = v
                "humi" -> humi = v
                "humi_set" -> humiSetpoint = v
                "mode" -> (modes.find { it.second == value })?.let {
                    mode = it.second
                }
                else -> hasReceived = false
            }

            if (hasReceived) this.hasReceived.postValue(field)
        }

        if (jsonResult.isEmpty()) {
            val value = data.second.toString()
            parse(
                value, when (data.first) {
                    mqtt.subs["temp"] -> "temp"
                    mqtt.subs["temp_set"] -> "temp_set"
                    mqtt.subs["humi"] -> "humi"
                    mqtt.subs["humi_set"] -> "humi_set"
                    mqtt.subs["mode"] -> "mode"
                    else -> null
                }
            )
        } else {
            for (e in jsonResult) {
                parse(e.value, e.key)
            }
        }
    }
}