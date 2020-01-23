package com.archaeology.models.firebase

import android.content.Context
import com.archaeology.models.SiteModel
import com.archaeology.models.UserStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class UserFireStore(val context: Context) : UserStore, AnkoLogger {

    val sites = ArrayList<SiteModel>()
    private lateinit var userId: String
    private lateinit var db: DatabaseReference
    private lateinit var st: StorageReference

    fun fetchSites(sitesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(sites) {
                    it.getValue<SiteModel>(SiteModel::class.java)
                }
                sitesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        sites.clear()
        db.child("users").child(userId).child("sites")
            .addListenerForSingleValueEvent(valueEventListener)
    }

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