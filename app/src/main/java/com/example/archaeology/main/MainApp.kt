package com.example.archaeology.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.example.archaeology.models.SiteMemStore

class MainApp : Application(), AnkoLogger {

  val sites = SiteMemStore()

  override fun onCreate() {
    super.onCreate()
    info("Site started")
  }
}