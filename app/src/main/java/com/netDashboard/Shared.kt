package com.netDashboard

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.widget.Toast

fun createToast(context: Context, msg: String) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    } else {
        Handler(Looper.getMainLooper()).post { Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
    }
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}