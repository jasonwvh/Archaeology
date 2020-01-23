package com.archaeology.main

import android.app.Application
import com.archaeology.models.SiteStore
import com.archaeology.models.UserStore
import com.archaeology.models.firebase.SiteFireStore
import com.archaeology.models.firebase.UserFireStore
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

  lateinit var sites: SiteStore
  lateinit var users: UserStore
  var activeUser: FirebaseUser? = null

  override fun onCreate() {
    super.onCreate()
    //sites = SiteJSONStore(applicationContext)
    sites = SiteFireStore(applicationContext)
    users = UserFireStore(applicationContext)
    info("Site started")
  }
}