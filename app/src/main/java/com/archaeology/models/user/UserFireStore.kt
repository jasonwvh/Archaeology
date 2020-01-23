package com.archaeology.models.user

import android.content.Context
import com.archaeology.models.site.SiteModel
import org.jetbrains.anko.AnkoLogger

class UserFireStore(val context: Context) : UserStore, AnkoLogger {
    val sites = ArrayList<SiteModel>()

    override fun logout() {
        sites.clear()
    }
}