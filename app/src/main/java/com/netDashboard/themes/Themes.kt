package com.netDashboard.themes

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.netDashboard.R
import com.netDashboard.getRandomColor


object Themes {

    fun set(context: Context) = context.setTheme(R.style.Theme_Dark)

    fun applyTheme(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val v = viewGroup.getChildAt(i)

            if (v is ViewGroup) applyTheme(v)
            v.defineType()
        }
        viewGroup.defineType()
    }

    private fun View.defineType() {
        when (this) {
            is MaterialButton -> this.applyTheme()
            is ImageView -> this.applyTheme()
            is TextView -> this.applyTheme()
            is SwitchMaterial -> this.applyTheme()
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
    }

    private fun RecyclerView.applyTheme() {
    }

    private fun FrameLayout.applyTheme() {
    }

    private fun LinearLayout.applyTheme() {
        this.setBackgroundColor(getRandomColor())
    }

    private fun View.applyTheme() {
        this.setBackgroundColor(getRandomColor())
        this.background = ColorDrawable(getRandomColor())
    }

    private fun MaterialButton.applyTheme() {
        this.backgroundTintList =
            ColorStateList.valueOf(
                getRandomColor()
            )
    }

    private fun ImageView.applyTheme() {
        this.backgroundTintList =
            ColorStateList.valueOf(
                getRandomColor()
            )
    }

    private fun TextView.applyTheme() {
        this.setTextColor(getRandomColor())
        this.backgroundTintList =
            ColorStateList.valueOf(
                getRandomColor()
            )
    }

    private fun SwitchMaterial.applyTheme() {
        this.backgroundTintList =
            ColorStateList.valueOf(
                getRandomColor()
            )
    }

    private fun EditText.applyTheme() {
        this.backgroundTintList =
            ColorStateList.valueOf(
                getRandomColor()
            )
    }

    private fun Chip.applyTheme() {
        this.backgroundTintList =
            ColorStateList.valueOf(
                getRandomColor()
            )
    }

    private fun Slider.applyTheme() {
        this.trackActiveTintList = ColorStateList.valueOf(
            getRandomColor()
        )
    }
}