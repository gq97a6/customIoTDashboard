package com.netDashboard

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.netDashboard.abyss.Abyss
import com.netDashboard.dashboard_activity.DashboardActivity
import com.netDashboard.databinding.MainActivityBinding
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {
    private lateinit var b: MainActivityBinding
    private var abyss = AbyssHandler(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = MainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)

        Log.i("OUY", Color.parseColor("#FFFFFFFF").toString())

        val con = this
        Timer().schedule(500) {
            Intent(con, DashboardActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
//
        //b.bind.setOnClickListener() {
        //    abyss.bind()
        //}
//
        //b.get.setOnClickListener() {
        //    if (abyss.isBounded) {
        //        val data:String = abyss.service.udpd.getData()
        //        Toast.makeText(this, "number: $data", Toast.LENGTH_SHORT).show()
        //    }
        //}
//
        //b.start.setOnClickListener() {
        //    abyss.start()
        //}
//
        //b.stop.setOnClickListener() {
        //    abyss.stop()
        //}
//
        //b.go.setOnClickListener() {
        //    Intent(this, DashboardActivity::class.java).also {
        //        startActivity(it)
        //    }
        //    finish()
        //}
//
        //Intent(this, DashboardActivity::class.java).also {
        //    startActivity(it)
        //}
        //finish()
    }

    //override fun onStart() {
    //    super.onStart()
    //    abyss.start()
    //    abyss.bind()
    //}
//
    //override fun onDestroy() {
    //    abyss.unbound()
    //    super.onDestroy()
    //}
//
    //override fun onStop() {
    //    abyss.unbound()
    //    super.onStop()
    //}

    inner class AbyssHandler(var context: Context) {
        var isBounded: Boolean = false
        lateinit var service: Abyss

        private val connection = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName, IBinder: IBinder) {
                val binder = IBinder as Abyss.AbyssBinder
                service = binder.getService()
                isBounded = true
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                isBounded = false
            }
        }

        fun start() {
            Intent(context, Abyss::class.java).also {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(it)
                } else {
                    startService(it)
                }
            }
        }

        fun stop() {
            Intent(context, Abyss::class.java).also {
                stopService(it)
            }
        }

        fun bind() {
            Intent(context, Abyss::class.java).also {
                bindService(it, connection, Context.BIND_AUTO_CREATE)
            }
        }

        fun unbound() {
            if (isBounded) {
                unbindService(connection)
                isBounded = false
            }
        }
    }
}
