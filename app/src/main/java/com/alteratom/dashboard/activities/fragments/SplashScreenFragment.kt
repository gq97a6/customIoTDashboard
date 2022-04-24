package com.alteratom.dashboard.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.alteratom.R
import com.alteratom.dashboard.G
import com.alteratom.dashboard.G.settings
import com.alteratom.dashboard.G.theme
import com.alteratom.dashboard.activities.MainActivity
import com.alteratom.databinding.FragmentSplashScreenBinding


class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {
    private lateinit var b: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        theme.apply(b.root, requireContext())

        b.ssIcon.setBackgroundResource(if (theme.a.isDark) R.drawable.ic_icon_light else R.drawable.ic_icon)
        b.ssIcon.animate()
            .rotationBy(100f)
            .scaleX(1.3f)
            .scaleY(1.3f)
            .withStartAction {
                (activity as MainActivity).apply {
                    supportFragmentManager.commit {
                        setCustomAnimations(
                            R.anim.splashscreen_in,
                            R.anim.splashscreen_out,
                            R.anim.splashscreen_in,
                            R.anim.splashscreen_out
                        )
                        replace(
                            R.id.m_fragment,
                            if (settings.startFromLast && G.setCurrentDashboard(settings.lastDashboardId))
                                DashboardFragment()
                            else MainScreenFragment()
                        )
                    }
                }
            }
            .setInterpolator(AccelerateDecelerateInterpolator())?.duration = 600
    }
}