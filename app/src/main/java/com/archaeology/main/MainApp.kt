package com.archaeology.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.archaeology.models.json.SiteJSONStore
import com.archaeology.models.SiteStore

class MainApp : Application(), AnkoLogger {

  lateinit var sites: SiteStore

  override fun onCreate() {
    super.onCreate()
    sites = SiteJSONStore(applicationContext)
    info("Site started")
  }
}