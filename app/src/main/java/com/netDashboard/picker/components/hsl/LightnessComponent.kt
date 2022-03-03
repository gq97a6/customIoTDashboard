package com.madrapps.pikolo.components.hsl

import android.graphics.Color.HSVToColor
import androidx.core.graphics.ColorUtils
import com.madrapps.pikolo.components.ArcComponent
import com.netDashboard.picker.Metrics
import com.netDashboard.picker.Paints

internal class LightnessComponent(
    metrics: Metrics,
    paints: Paints,
    arcLength: Float,
    arcStartAngle: Float
) : ArcComponent(metrics, paints, arcLength, arcStartAngle) {

    override val componentIndex: Int = 2
    override val range: Float = 1f
    override val noOfColors = 2
    override val colors = IntArray(noOfColors)
    override val colorPosition = FloatArray(noOfColors)

    override fun updateColorArray(color: FloatArray): IntArray {
        for (i in 0 until noOfColors) {
            color[componentIndex] = i.toFloat() / (noOfColors - 1)
            colors[i] = HSVToColor(color)
        }
        return colors
    }
}