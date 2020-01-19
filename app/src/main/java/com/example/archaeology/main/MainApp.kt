package com.example.archaeology.main

import android.app.Application
import com.example.archaeology.models.SiteJSONStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.example.archaeology.models.SiteMemStore
import com.example.archaeology.models.SiteStore

class MainApp : Application(), AnkoLogger {

  lateinit var sites : SiteStore

  override fun onCreate() {
    super.onCreate()
    //sites = SiteMemStore()
    sites = SiteJSONStore(applicationContext)
    info("Site started")
  }
}