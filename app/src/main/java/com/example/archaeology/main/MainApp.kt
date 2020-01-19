package com.example.archaeology.main

import android.app.Application
import com.example.archaeology.models.SiteModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    val sites = ArrayList<SiteModel>()

    override fun onCreate() {
        super.onCreate()
        info("Site started")
        sites.add(SiteModel("One", "About one..."))
        sites.add(SiteModel("Two", "About two..."))
        sites.add(SiteModel("Three", "About three..."))
    }
}