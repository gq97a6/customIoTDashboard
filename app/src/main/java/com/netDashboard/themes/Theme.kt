@file:Suppress("ControlFlowWithEmptyBody")

package com.netDashboard.themes

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.netDashboard.*

object Theme {

    var isGlobal = false
    var isDark = true
    var color = Color.parseColor("#00dba1")

    private val isDarkRec: Boolean
        get() {
            val lConCheck = (ColorUtils.calculateContrast(color, getBackground(false)) > 2.4)
            val dConCheck = (ColorUtils.calculateContrast(color, getBackground(true)) > 1.4)

            return if (lConCheck && dConCheck) isDark else lConCheck
        }

    private val colorA
        get() = ColorUtils.blendARGB(color, colorBackground, 0.4f)
    private val colorB
        get() = ColorUtils.blendARGB(color, colorBackground, 0.6f)
    private val colorC
        get() = ColorUtils.blendARGB(color, colorBackground, 0.8f)
    private val colorD
        get() = ColorUtils.blendARGB(color, colorBackground, 0.9f)

    private val colorBackground: Int
        get() = getBackground(!isDark)

    private fun getBackground(isDark: Boolean): Int {
        val hsv = floatArrayOf(0f, 0f, 0f)
        Color.colorToHSV(color, hsv)
        hsv[1] = hsv[1] * 0.3f

        return Color.HSVToColor(hsv).contrast(isDark, 0.6f)
    }

    fun apply(context: Context, viewGroup: ViewGroup) {

        context.setTheme(if (!isDark) R.style.Theme_Dark else R.style.Theme_Light)

        WindowInsetsControllerCompat(
            (context as Activity).window,
            viewGroup
        ).isAppearanceLightStatusBars = !isDark

        context.window.statusBarColor = colorBackground

        viewGroup.applyTheme()
    }

    private fun ViewGroup.applyTheme() {
        for (i in 0 until this.childCount) {
            val v = this.getChildAt(i)

            if (v is ViewGroup) v.applyTheme()
            v.defineType()
        }

        this.defineType()
    }

    private fun View.defineType() {
        when (this) {
            is MaterialButton -> this.applyTheme()
            is SwitchMaterial -> this.applyTheme()
            is TextView -> this.applyTheme()
            is EditText -> this.applyTheme()
            is Chip -> this.applyTheme()
            is Slider -> this.applyTheme()
            is LinearLayout -> this.applyTheme()
            is FrameLayout -> this.applyTheme()
            is RecyclerView -> this.applyTheme()
            is ChipGroup -> this.applyTheme()
            else -> {
                if (this.javaClass == View::class.java) this.applyTheme()
                else Log.i("OUY", "View type not specified: ${this.javaClass}")
            }
        }
    }

    private fun ChipGroup.applyTheme() {
        when (this.tag) {
            else -> onUnknownTag(this.tag, "chipGroup")
        }
    }

    private fun RecyclerView.applyTheme() {
        when (this.tag) {
            else -> onUnknownTag(this.tag, "recyclerView")
        }
    }

    private fun FrameLayout.applyTheme() {
        when (this.tag) {
            else -> onUnknownTag(this.tag, "frameLayout")
        }
    }

    private fun LinearLayout.applyTheme() {
        when (this.tag) {
            "background" -> this.setBackgroundColor(colorBackground)
            "frame" -> {
                val drawable = this.background as? GradientDrawable
                drawable?.setStroke(1, color)
            }
            else -> onUnknownTag(this.tag, "linearLayout")
        }
    }

    private fun View.applyTheme() {
        when (this.tag) {
            "color" -> this.setBackgroundColor(color)
            "colorA" -> this.setBackgroundColor(colorA)
            "colorB" -> this.setBackgroundColor(colorB)
            "colorC" -> this.setBackgroundColor(colorC)
            "contrast205" -> this.backgroundTintList = ColorStateList.valueOf(
                contrastColor(
                    !isDark, 205
                )
            )
            else -> onUnknownTag(this.tag, "view")
        }
    }

    private fun MaterialButton.applyTheme() {
        this.backgroundTintList = ColorStateList.valueOf(color)

        when (this.tag) {
            "color" -> this.backgroundTintList = ColorStateList.valueOf(color)
            "colorA" -> this.backgroundTintList = ColorStateList.valueOf(colorA)
            else -> onUnknownTag(this.tag, "materialButton")
        }
    }

    private fun TextView.applyTheme() {
        when (this.tag) {
            "color" -> this.setTextColor(color)
            "colorA" -> this.setTextColor(colorA)
            "colorB" -> this.setTextColor(colorB)
            "colorC" -> this.setTextColor(colorC)
            "con_warning" -> {
                this.clearAnimation()
                this.visibility = if (isDark != isDarkRec) {
                    this.setTextColor(color.contrast(isDark, 0.6f))
                    this.blink(-1, 20, 500)

                    VISIBLE
                } else {
                    GONE
                }
            }
            else -> onUnknownTag(this.tag, "textView")
        }
    }

    //todo
    private fun SwitchMaterial.applyTheme() {
        when (this.tag) {
            else -> onUnknownTag(this.tag, "switchMaterial")
        }

        this.backgroundTintList =
            ColorStateList.valueOf(
                getRandomColor()
            )

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )

        val colors = intArrayOf(
            colorD,
            colorB
        )

        val list = ColorStateList(states, colors)
        this.thumbTintList = list
        this.trackTintList =
            ColorStateList.valueOf(
                color
            )
    }

    private fun EditText.applyTheme() {
        when (this.tag) {
            else -> onUnknownTag(this.tag, "editText")
        }
        this.setTextColor(colorC)
    }

    private fun Chip.applyTheme() {
        when (this.tag) {
            else -> onUnknownTag(this.tag, "chip")
        }
        this.backgroundTintList =
            ColorStateList.valueOf(
                getRandomColor()
            )
    }

    private fun Slider.applyTheme() {
        when (this.tag) {
            else -> onUnknownTag(this.tag, "slider")
        }
        this.trackActiveTintList = ColorStateList.valueOf(colorC)
    }

    private fun onUnknownTag(tag: Any?, type: String) {
        tag?.toString()?.let {
            if (it.isNotBlank()) Log.i("OUY", "Unknown $type tag: $it")
        }
    }
}