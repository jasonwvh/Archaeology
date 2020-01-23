package com.archaeology.models.firebase

import android.content.Context
import com.archaeology.models.SiteModel
import com.archaeology.models.UserStore
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class UserFireStore(val context: Context) : UserStore, AnkoLogger {

    val sites = ArrayList<SiteModel>()
    private lateinit var userId: String
    private lateinit var st: StorageReference

    override fun logout() {
        sites.clear()
    }

    private fun deleteUserImages() {
        sites.forEach {
            val images = st.child(userId).child(it.fbId)
            images.listAll().addOnSuccessListener { folder ->
                folder.items.forEach { item ->
                    item.delete()
                }
            }.addOnFailureListener { exception ->
                info("Error deleting photos: $exception")
            }
        }
    }

}