package com.archaeology.models.firebase

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.archaeology.helpers.readImageFromPath
import com.archaeology.models.SiteModel
import com.archaeology.models.SiteStore
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream
import java.io.File


class SiteFireStore(val context: Context) : SiteStore, AnkoLogger {
    val sites = ArrayList<SiteModel>()

    private lateinit var userId: String
    private lateinit var db: DatabaseReference
    private lateinit var st: StorageReference

    override fun findAllSites(): ArrayList<SiteModel>? {
        return sites
    }

    override fun findOneSite(siteID: Int): SiteModel? {
        return sites.find { p -> p.id == siteID }
    }

    @SuppressLint("DefaultLocale")
    override fun findSitesByName(name: String): ArrayList<SiteModel>? {
        val foundSites: ArrayList<SiteModel>? = arrayListOf()
        sites.forEach {
            if (it.name.toLowerCase().contains(name.toLowerCase())) {
                info("FOUND $name")
                foundSites?.add(it)
            }
        }
        return foundSites
    }

    override fun createSite(site: SiteModel) {
        val key = db.child("users").child(userId).child("sites").push().key

        key?.let {
            site.fbId = key
            sites.add(site)
            db.child("users").child(userId).child("sites").child(key).setValue(site)
            updateImage(site)
        }
    }

    override fun updateSite(site: SiteModel) {
        val foundSite: SiteModel? = sites.find { p -> p.fbId == site.fbId }
        if (foundSite != null) {
            foundSite.name = site.name
            foundSite.description = site.description
            foundSite.images = site.images
            foundSite.location = site.location
            foundSite.dateVisited = site.dateVisited
            foundSite.visited = site.visited
            foundSite.notes = site.notes
            foundSite.rating = site.rating
            foundSite.isFavourite = site.isFavourite
        }
        db.child("users").child(userId).child("sites").child(site.fbId).setValue(site)
        updateImage(site)
        sites[sites.indexOf(site)] = site
    }

    override fun deleteSite(site: SiteModel) {
        db.child("users").child(userId).child("sites").child(site.fbId).removeValue()
        // remove images
        deleteSiteImages(site.fbId)

        // remove from user favourites
        db.child("users").child(userId).child("favourites").child(site.fbId).removeValue()

        sites.remove(site)
    }

    private fun deleteSiteImages(siteFBID: String) {
        val images = st.child(userId).child(siteFBID)
        images.listAll().addOnSuccessListener { folder ->
            folder.items.forEach { item ->
                item.delete()
            }
        }.addOnFailureListener { exception ->
            info("Error deleting photos: $exception")
        }
    }

    private fun updateImage(site: SiteModel) {

        site.images.forEach { image ->

            var bitmap: Bitmap?

            val fileName = File(image.uri)
            val imageName = fileName.name
            val imageRef = st.child("$userId/${site.fbId}/$imageName")
            val byteOutputStream = ByteArrayOutputStream()

            // if image uri contains .jpg, it came from camera
            if (image.uri.contains(".jpg")) {
                bitmap = BitmapFactory.decodeFile(image.uri)
                if (bitmap.height > 2000 || bitmap.width > 2000) {
                    bitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        bitmap.width / 2,
                        bitmap.height / 2,
                        false
                    )
                }
            } else {
                bitmap = readImageFromPath(context, image.uri)
            }

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream)
                val data = byteOutputStream.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener { failure ->
                    println("FAILURE: ${failure.message}")
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                        image.uri = uri.toString()
                        image.fbID = site.fbId
                        db.child("users").child(userId).child("sites").child(site.fbId)
                            .setValue(site)
                    }
                }
            }
        }
    }

    override fun sortByRating(): List<SiteModel>? {
        return sites.sortedWith(compareBy { it.rating }).asReversed()
    }

    override fun sortByVisit(): List<SiteModel>? {
        return sites.sortedWith(compareBy { it.visited }).asReversed()
    }

}