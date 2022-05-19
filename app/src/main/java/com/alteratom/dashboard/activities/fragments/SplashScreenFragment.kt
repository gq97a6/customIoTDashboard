package com.alteratom.dashboard.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.alteratom.dashboard.G.theme
import com.alteratom.dashboard.Theme
import com.alteratom.dashboard.compose.ComposeTheme
import kotlin.math.*

class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        theme.apply(context = requireContext())

        return ComposeView(requireContext()).apply {
            setContent {

                ComposeTheme(Theme.isDark) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Surface(
                            modifier = Modifier
                                .size(300.dp)
                                .background(color = Color.White)
                        ) {
                            RoundSlider(modifier = Modifier.padding(20.dp))
                        }
                    }
                }
            }
/*
            setContent {
                var serviceReady by remember { mutableStateOf(false) }

                ComposeTheme(Theme.isDark) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Theme.colors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            val icon = if (Theme.isDark) R.drawable.ic_icon_light
                            else R.drawable.ic_icon

                            val rotation = if (serviceReady) 200f else 0f
                            val rotate: Float by animateFloatAsState(
                                targetValue = rotation,
                                animationSpec = tween(durationMillis = 600, easing = LinearEasing)
                            )

                            val size = if (serviceReady) 2f else 1f
                            val scale: Float by animateFloatAsState(
                                targetValue = size,
                                animationSpec = tween(durationMillis = 600, easing = LinearEasing)
                            )

                            Image(
                                painterResource(icon), "Logo",
                                modifier = Modifier
                                    .size(300.dp)
                                    .scale(scale)
                                    .rotate(rotate),
                                colorFilter = ColorFilter.tint(
                                    Theme.colors.color.copy(alpha = .4f),
                                    BlendMode.SrcAtop
                                )
                            )

                            Spacer(modifier = Modifier.fillMaxHeight(.2f))
                        }
                    }
                }

                if (serviceReady) {
                    if (settings.startFromLast && G.setCurrentDashboard(settings.lastDashboardId))
                        fm.replaceWith(DashboardFragment(), false, fadeLong)
                    else fm.popBackStack(false, fadeLong)
                }

                serviceReady = true
            }

        }
        */
        }
    }
}

@Composable
fun RoundSlider(modifier: Modifier = Modifier) {
    var dragPosition by remember { mutableStateOf(Offset.Zero) }
    var changeVal by remember { mutableStateOf(Offset.Zero) }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    dragPosition += dragAmount
                    changeVal = change.position

                    change.consumeAllChanges()
                }
            }
            .background(color = Color.Green.copy(alpha = 0.2f))
    ) {
        val xCenter = size.width / 2
        val yCenter = size.height / 2

        val radius = minOf(xCenter, yCenter)

        Log.i(
            "OUY",
            "${calculateAngle(changeVal.x, changeVal.y, center.x, center.y, 0f, 180f, 180f)}"
        )

        drawCircle(color = Color.Green, radius = 15.dp.toPx(), center = center)
        drawCircle(color = Color.Green, radius = 15.dp.toPx(), center = changeVal)

        fun calculateIndicatorPosition(dragPosition: Offset): Offset {
            val x = dragPosition.x - xCenter
            val y = dragPosition.y - yCenter

            var angle = sqrt(x.pow(2) + y.pow(2)).let { r ->
                acos(x / r).let {
                    if (y < 0) -it else it
                }
            }

            return Offset(radius * cos(angle), radius * sin(angle))
        }

        drawArc(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color.Red,
                    Color.Yellow,
                    Color.Green,
                    Color.Cyan,
                    Color.Blue,
                    Color.Magenta,
                    Color.Red,
                    Color.Red,
                    Color.Red,
                    Color.Red,
                    Color.Red,
                    Color.Red,
                    Color.Red,
                    Color.Red
                ),
                center = center
            ),
            startAngle = -0f,
            sweepAngle = 180f,
            useCenter = false,
            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
        )

        val (indicatorX, indicatorY) = calculateIndicatorPosition(dragPosition)
        translate(indicatorX, indicatorY) {
            drawCircle(color = Color.Black, radius = 15.dp.toPx())
        }

        val inX = (center.x + radius * cos(Math.toRadians(-90.0))).toFloat()
        val inY = (center.y + radius * sin(Math.toRadians(-90.0))).toFloat()
        drawCircle(color = Color.Yellow, radius = 15.dp.toPx(), center = Offset(inX, inY))
    }
}

fun calculateAngleInContinuousRange(
    middle: Float,
    angle: Double,
    arcStartAngle: Float,
    arcEndAngle: Float
): Double {
    return when (angle) {
        in arcEndAngle..middle -> arcEndAngle.toDouble()
        in middle..arcStartAngle -> arcStartAngle.toDouble()
        else -> 20.toDouble()
    }
}

fun calculateAngleInNonContinuousRange(
    middle: Float,
    angle: Double,
    arcStartAngle: Float,
    arcEndAngle: Float
): Double {
    return if (middle > 360f) {
        val correctedMiddle = middle - 360f
        when (angle) {
            in arcEndAngle..360f, in 0f..correctedMiddle -> arcEndAngle.toDouble()
            in correctedMiddle..arcStartAngle -> arcStartAngle.toDouble()
            else -> 10.toDouble()
        }
    } else {
        when (angle) {
            in arcEndAngle..middle -> arcEndAngle.toDouble()
            in middle..360f, in 0f..arcStartAngle -> arcStartAngle.toDouble()
            else -> 10.toDouble()
        }
    }
}

fun calculateAngle(
    x: Float,
    y: Float,
    xCenter: Float,
    yCenter: Float,
    arcStartAngle: Float,
    arcEndAngle: Float,
    sweepAngle: Float
): Double {
    val x = x - xCenter
    val y = y - yCenter
    val c = sqrt((x * x + y * y).toDouble())

    var angle = Math.toDegrees(acos(x / c))
    if (y < 0) {
        angle = 360 - angle
    }

    // Don't let the indicator go outside the arc
    // limit the indicator between arcStartAngle and arcEndAngle
    val associatedArcLength = 360f - sweepAngle
    val middleOfAssociatedArc = arcEndAngle + associatedArcLength / 2f
    if (arcEndAngle < arcStartAngle) {
        angle = calculateAngleInContinuousRange(
            middleOfAssociatedArc,
            angle,
            arcStartAngle,
            arcEndAngle
        )
    } else if (arcEndAngle > arcStartAngle) {
        angle = calculateAngleInNonContinuousRange(
            middleOfAssociatedArc,
            angle,
            arcStartAngle,
            arcEndAngle
        )
    }

    return angle
}