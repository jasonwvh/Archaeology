package com.archaeology.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import com.archaeology.models.SiteModel
import com.archaeology.models.SiteStore

class SiteFireStore(val context: Context) : SiteStore, AnkoLogger {

    val sites = ArrayList<SiteModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override fun findAll(): List<SiteModel> {
        return sites
    }

    override fun findById(id: Long): SiteModel? {
        val foundSite: SiteModel? = sites.find { p -> p.id == id }
        return foundSite
    }

    override fun create(site: SiteModel) {
        val key = db.child("users").child(userId).child("sites").push().key
        key?.let {
            site.fbId = key
            sites.add(site)
            db.child("users").child(userId).child("sites").child(key).setValue(site)
        }
    }

    override fun update(site: SiteModel) {
        var foundSite: SiteModel? = sites.find { p -> p.fbId == site.fbId }
        if (foundSite != null) {
            foundSite.name = site.name
            foundSite.description = site.description
            foundSite.image = site.image
            foundSite.location = site.location
        }

        db.child("users").child(userId).child("sites").child(site.fbId).setValue(site)

    }

    override fun delete(site: SiteModel) {
        db.child("users").child(userId).child("sites").child(site.fbId).removeValue()
        sites.remove(site)
    }

    override fun clear() {
        sites.clear()
    }

    fun fetchSites(sitesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(sites) { it.getValue<SiteModel>(SiteModel::class.java) }
                sitesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        sites.clear()
        db.child("users").child(userId).child("sites").addListenerForSingleValueEvent(valueEventListener)
    }
}