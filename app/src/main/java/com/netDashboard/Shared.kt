package com.netDashboard

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar


fun createToast(context: Context, msg: String, time: Int = 0) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(context, msg, time).show()
    } else {
        Handler(Looper.getMainLooper()).post { Toast.makeText(context, msg, time).show() }
    }
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Snackbar.margins(): Snackbar{
    val params = this.view.layoutParams as android.widget.FrameLayout.LayoutParams

    params.setMargins(30.toPx(),30.toPx(),30.toPx(),30.toPx())

    this.view.layoutParams = params

    return this
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}