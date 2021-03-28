package com.example.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat

fun createToast(context: Context, msg: String) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    } else {
        Handler(Looper.getMainLooper()).post { Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
    }
}