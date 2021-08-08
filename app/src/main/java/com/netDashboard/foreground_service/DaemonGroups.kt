package com.netDashboard.foreground_service

import android.content.Context
import com.netDashboard.dashboard.Dashboard
import com.netDashboard.dashboard.Dashboards
import com.netDashboard.foreground_service.demons.Mqttd

class DaemonGroups(context: Context) {

    private val dashboards = Dashboards.getList()
    private val collection: MutableList<DaemonGroup> = mutableListOf()

    init {
        for (d in dashboards) {
            val g = DaemonGroup(context, d)
            collection.add(g)
        }
    }

    fun get(name: String): DaemonGroup? {
        for (dg in collection) {
            if (dg.dashboard.name == name) return dg
        }
        return null
    }
}

class DaemonGroup(context: Context, val dashboard: Dashboard) {
    var mqttd = Mqttd(context, dashboard)
}