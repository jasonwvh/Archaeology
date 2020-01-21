package com.archaeology.main

import android.app.Application
import com.archaeology.models.SiteStore
import com.archaeology.models.room.SiteStoreRoom
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

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