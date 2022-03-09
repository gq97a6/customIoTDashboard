package com.netDashboard.tile.types.slider

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.fasterxml.jackson.annotation.JsonIgnore
import com.netDashboard.DialogBuilder.dialogSetup
import com.netDashboard.R
import com.netDashboard.databinding.DialogSliderBinding
import com.netDashboard.globals.G.theme
import com.netDashboard.recycler_view.RecyclerViewAdapter
import com.netDashboard.roundCloser
import com.netDashboard.screenWidth
import com.netDashboard.tile.Tile
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.math.abs

class SliderTile : Tile() {

    @JsonIgnore
    override val layout = R.layout.tile_slider

    @JsonIgnore
    override var typeTag = "slider"

    override var iconKey = "il_arrow_arrows_h_alt"

    var range = mutableListOf(0, 100, 10)
    var dragCon = false

    var value: Int = 0
        set(value) {
            field = value
            displayValue = value
        }

    @JsonIgnore
    var displayValue: Int = 0
        set(value) {
            field = value
            val dp = holder?.itemView?.findViewById<TextView>(R.id.ts_value)
            dp?.text = value.toString()

            setBackground(value, holder?.itemView?.findViewById(R.id.ts_background))
        }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (range[2] == 0) range[2] = 1
        displayValue = value
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onClick(v: View, e: MotionEvent) {
        super.onClick(v, e)

        if (mqttData.pubs["base"].isNullOrEmpty()) return
        if (dashboard.dg?.mqttd?.client?.isConnected != true) return

        if (!dragCon) {
            val dialog = Dialog(adapter.context)

            dialog.setContentView(R.layout.dialog_slider)
            val binding = DialogSliderBinding.bind(dialog.findViewById(R.id.root))

            binding.dsValue.text = value.toString()
            setBackground(value, binding.tsBackground)

            binding.root.setOnTouchListener { _, event ->
                control(event, v.parent as View).let {
                    if (it.second) dialog.dismiss()
                    else {
                        setBackground(it.first, binding.tsBackground)
                        binding.dsValue.text = it.first.toString()
                    }
                    return@setOnTouchListener true
                }
            }

            dialog.dialogSetup()
            theme.apply(binding.root, anim = false)
            dialog.show()
        }
    }

    override fun onTouch(v: View, e: MotionEvent) {
        super.onTouch(v, e)

        if (dragCon) {
            control(e, holder?.itemView).let {
                displayValue = if (it.second) value else it.first
            }
        }
    }

    override fun onReceive(
        data: Pair<String?, MqttMessage?>,
        jsonResult: MutableMap<String, String>
    ) {
        (jsonResult["base"] ?: data.second.toString()).toIntOrNull()
            ?.let { this.value = it.roundCloser(range[2]) }
    }

    private fun control(e: MotionEvent, v: View?): Pair<Int, Boolean> {
        var p = 100f * (e.rawX - screenWidth * 0.2f) / (screenWidth * (0.8f - 0.2f))
        if (p < 0) p = 0f
        else if (p > 100) p = 100f

        val value = (range[0] + p * (range[1] - range[0]) / 100).toInt().roundCloser(range[2])

        when (e.action) {
            ACTION_DOWN -> (v as ViewGroup?)?.requestDisallowInterceptTouchEvent(true)
            ACTION_UP -> {
                (v as ViewGroup?)?.requestDisallowInterceptTouchEvent(false)
                send((mqttData.payloads["base"] ?: "").replace("@value", value.toString()))
                return Pair(value, true)
            }
        }

        return Pair(value, false)
    }

    private fun setBackground(value: Int, background: View?) {
        val params = background?.layoutParams as ConstraintLayout.LayoutParams?
        params?.matchConstraintPercentWidth =
            abs((((range[0] - value).toFloat() / (range[1] - range[0]))))
        background?.requestLayout()
    }

    override fun onCreateTile() {
        super.onCreateTile()

        mqttData.payloads["base"] = "@value"
    }
}