package com.madrapps.pikolo.components.hsl

import android.graphics.Color.HSVToColor
import androidx.core.graphics.ColorUtils
import com.madrapps.pikolo.components.ArcComponent
import com.netDashboard.picker.Metrics
import com.netDashboard.picker.Paints

internal class SaturationComponent(
    metrics: Metrics,
    paints: Paints,
    arcLength: Float,
    arcStartAngle: Float
) : ArcComponent(metrics, paints, arcLength, arcStartAngle) {

    override val componentIndex: Int = 1
    override val range: Float = 1f
    override val noOfColors = 2
    override val colors = IntArray(2)
    override val colorPosition = FloatArray(noOfColors)

    override fun updateColorArray(color: FloatArray): IntArray {
        for (i in 0 until noOfColors) {
            color[componentIndex] = i.toFloat() / (noOfColors - 1)
            colors[i] = HSVToColor(color)
        }
        return colors
    }
}