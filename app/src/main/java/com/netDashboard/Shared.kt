@file:Suppress("UNUSED")

package com.netDashboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.*
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import java.util.*

const val A = 255 //100%
const val B = 150 //60%
const val C = 75 //30%
const val D = 25 //10%

val screenHeight = Resources.getSystem().displayMetrics.heightPixels
val screenWidth = Resources.getSystem().displayMetrics.widthPixels

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Float.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Float.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun getRandomColor(alpha: Int = 255, R: Int = 255, G: Int = 255, B: Int = 255): Int {
    val r = Random()
    return Color.argb(alpha, r.nextInt(R + 1), r.nextInt(G + 1), r.nextInt(B + 1))
}

infix fun Int.alpha(@IntRange(from = 0, to = 255) a: Int): Int =
    Color.argb(a, this.red, this.green, this.blue)

fun Int.isDark(): Boolean {

    val whiteContrast = ColorUtils.calculateContrast(this, Color.WHITE)
    val blackContrast = ColorUtils.calculateContrast(this, Color.BLACK)

    return whiteContrast > blackContrast
}

fun contrastColor(isDark: Boolean, @IntRange(from = 0, to = 255) alpha: Int = 255): Int =
    (if (isDark) Color.WHITE else Color.BLACK).alpha(alpha)

fun Int.contrastColor(@IntRange(from = 0, to = 255) alpha: Int = 255): Int =
    (if (this.isDark()) Color.WHITE else Color.BLACK).alpha(alpha)

fun Int.contrast(
    @FloatRange(from = 0.0, to = 1.0) ratio: Float,
    @IntRange(from = 0, to = 255) alpha: Int = 255
): Int = this.contrast(this.isDark(), ratio, alpha)

fun Int.contrast(
    isDark: Boolean,
    @FloatRange(from = 0.0, to = 1.0) ratio: Float,
    @IntRange(from = 0, to = 255) alpha: Int = 255
): Int = ColorUtils.blendARGB(this, contrastColor(isDark), ratio).alpha(alpha)

infix fun Int.darkened(@FloatRange(from = 0.0, to = 1.0) by: Float): Int =
    ColorUtils.blendARGB(this, Color.BLACK, by)

infix fun Int.lightened(@FloatRange(from = 0.0, to = 1.0) by: Float): Int =
    ColorUtils.blendARGB(this, Color.WHITE, by)

fun Float.dezero(): String {
    return when (this - this.toInt()) {
        0f -> this.toInt()
        else -> this
    }.toString()
}

fun Int.roundCloser(step: Int): Int {
    return this / step * step
}

fun createNotification(
    context: Context,
    title: String = "Title",
    text: String = "Text",
    isSilent: Boolean = false,
    id: Int = Random().nextInt()
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel(context)

    val notification = NotificationCompat.Builder(context, "notification_id")
        .setAutoCancel(true)
        .setContentTitle(title)
        .setContentText(text)
        .setSmallIcon(R.drawable.icon_main)
        .setPriority(NotificationCompat.PRIORITY_MIN)
        .setVisibility(NotificationCompat.VISIBILITY_SECRET)

    if (isSilent) notification.setSilent(true)

    with(NotificationManagerCompat.from(context)) {
        notify(id, notification.build())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        "notification_id",
        "Other notification",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "com/netDashboard/notification_channel"
    }

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

@Suppress("DEPRECATION")
fun createVibration(context: Context, ms: Long = 500) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    if (vibrator.hasVibrator()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    ms,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(ms)
        }
    }
}

fun createToast(context: Context, msg: String, time: Int = 0) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(context, msg, time).show()
    } else {
        Handler(Looper.getMainLooper()).post { Toast.makeText(context, msg, time).show() }
    }
}

fun View.blink(
    times: Int = Animation.INFINITE,
    duration: Long = 50,
    offset: Long = 20,
    minAlpha: Float = 0.0f,
    maxAlpha: Float = 1.0f,
    repeatMode: Int = Animation.REVERSE
) {
    startAnimation(AlphaAnimation(maxAlpha, minAlpha).also {
        it.duration = duration
        it.startOffset = offset
        it.repeatMode = repeatMode
        it.repeatCount = times
    })
}

fun View.jiggle() = this.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.jiggle))
fun View.attentate() =
    this.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.attentate))

fun View.click() {
    this.performClick()
    this.isPressed = true
    this.invalidate()
    this.isPressed = false
    this.invalidate()
}

//@SuppressLint("ShowToast")
//val snackbar = list[0].holder?.itemView?.rootView?.let {
//    Snackbar.make(
//        it,
//        context.getString(R.string.snackbar_confirmation),
//        Snackbar.LENGTH_LONG
//    ).setAction("YES") {
//        for (i in list) {
//            if (i.flag.isRemove) onItemRemove(i)
//        }
//        list.removeAll { item: item -> item.flag.isRemove }
//        notifyDataSetChanged()
//    }
//}
//
//val snackBarView = snackbar?.view
//snackBarView?.translationY = -90.toPx().toFloat()
//snackbar?.show()