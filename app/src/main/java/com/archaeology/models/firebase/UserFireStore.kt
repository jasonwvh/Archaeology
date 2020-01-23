package com.archaeology.models.firebase

import android.content.Context
import com.archaeology.models.SiteModel
import com.archaeology.models.UserStore
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger

class UserFireStore(val context: Context) : UserStore, AnkoLogger {

    val sites = ArrayList<SiteModel>()
    private lateinit var userId: String
    private lateinit var st: StorageReference

    override fun logout() {
        sites.clear()
    }
}