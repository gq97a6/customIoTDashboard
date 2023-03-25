package com.alteratom.dashboard.activities

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.alteratom.dashboard.ForegroundService
import com.alteratom.dashboard.FragmentManager
import com.alteratom.dashboard.activities.fragments.SetupFragment
import com.alteratom.dashboard.objects.ActivityHandler
import com.alteratom.dashboard.objects.G
import com.alteratom.dashboard.objects.G.initializeGlobals
import com.alteratom.dashboard.switcher.FragmentSwitcher
import com.alteratom.dashboard.switcher.TileSwitcher
import com.alteratom.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var b: ActivityMainBinding

    var doOverrideOnBackPress: () -> Boolean = { false }

    companion object {
        lateinit var fm: FragmentManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHandler.onCreate(this)

        //Partially initialize globals if service has not been started
        if (ForegroundService.service?.isStarted != true) initializeGlobals(1)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        G.theme.apply(b.root, this, false)

        //Setup fragment manager and switchers
        fm = FragmentManager(this)
        fm.replaceWith(SetupFragment(), false, null)
        TileSwitcher.activity = this
        FragmentSwitcher.activity = this

        onBackPressedDispatcher.addCallback(this) {
            if (!doOverrideOnBackPress() && !fm.popBackStack()) finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityHandler.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        ActivityHandler.onPause()
    }
}
