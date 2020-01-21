package com.archaeology.main

import android.app.Application
import com.archaeology.models.SiteFireStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.archaeology.models.SiteStore

class MainApp : Application(), AnkoLogger {

  lateinit var sites: SiteStore

  override fun onCreate() {
    super.onCreate()
    //sites = SiteJSONStore(applicationContext)
    //sites = SiteStoreRoom(applicationContext)
    sites = SiteFireStore(applicationContext)
    info("Site started")
  }
}