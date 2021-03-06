package com.archaeology.views.site

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import com.archaeology.helpers.*
import com.archaeology.models.site.ImageModel
import com.archaeology.models.site.Location
import com.archaeology.models.site.NoteModel
import com.archaeology.models.site.SiteModel
import com.archaeology.views.BasePresenter
import com.archaeology.views.BaseView
import com.archaeology.views.VIEW
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_site_fab.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.*

class SitePresenter(view: BaseView) : BasePresenter(view) {
    private var site = SiteModel()

    var map: GoogleMap? = null
    val locationRequest = createDefaultLocationRequest()

    private var notes: ArrayList<NoteModel> = arrayListOf()
    private var images: ArrayList<ImageModel> = arrayListOf()

    private var edit = false
    private var first_time = true

    private val IMAGE_REQUEST = 1
    private val LOCATION_REQUEST = 2
    private val IMAGE_CAPTURE_REQUEST = 3

    private var locationService: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(view)
    private var location = Location()

    init {
        if (view.intent.hasExtra("site_edit")) {
            edit = true
            site = view.intent.extras?.getParcelable("site_edit")!!

            notes = site.notes
            images = site.images
            view.showSite(site)

        } else {
            if (checkLocationPermissions(view)) {
                if (first_time) {
                    doSetCurrentLocation()
                    first_time = false
                }
            }
        }
    }

    override fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (isPermissionGranted(requestCode, grantResults)) {
            if (first_time) {
                doSetCurrentLocation()
                first_time = false
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            if (it !== null) {
                locationUpdate(it.latitude, it.longitude)
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            if (first_time) {
                locationService.requestLocationUpdates(locationRequest, locationCallback, null)
                first_time = false
            }
        }
    }

    fun doClickNote(noteModel: NoteModel) {
        view?.alert("${noteModel.title}\n\n${noteModel.content}")?.show()
    }

    fun doAddOrSave(tempSite: SiteModel) {
        site.name = tempSite.name
        site.description = tempSite.description
        site.visited = tempSite.visited
        site.dateVisited = tempSite.dateVisited
        site.images = images
        site.notes = notes
        site.rating = tempSite.rating

        doAsync {
            if (edit) {
                app.sites.updateSite(site)
            } else {
                app.sites.createSite(site)
            }
            uiThread {
                view?.finish()
                view?.navigateTo(VIEW.LIST)
            }
        }
    }

    fun doDelete() {
        doAsync {
            app.sites.deleteSite(site)
            uiThread {
                view?.finish()
            }
        }
    }

    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doSetLocation() {

        view?.navigateTo(
            VIEW.LOCATION,
            LOCATION_REQUEST,
            "location",
            site.location
        )
    }

    fun doFavourite() {
        if (site.fbId == "") {
            view?.toast("Site needs to be created first")
        } else {
            site.isFavourite = !site.isFavourite
            app.sites.updateSite(site)
            app.sites.toggleFavourite(site)
            if (site.isFavourite) {
                view?.fabMoreFavourite!!.setColorFilter(Color.rgb(255, 116, 216))
                view?.toast("Added to Favourites list")
            } else {
                view?.toast("Removed from Favourites list")
                view?.fabMoreFavourite!!.setColorFilter(Color.rgb(255, 255, 255))
            }
        }
    }

    fun doAddNote(title: String, content: String) {
        val newNote = NoteModel()
        newNote.title = title
        newNote.content = content
        newNote.id = notes.size.plus(1)
        newNote.fbId = site.fbId
        doAsync {
            notes.add(newNote)
            site.notes = notes
            uiThread {
                view?.showNotes(notes)
            }
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(site.location.lat, site.location.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {

        print("LOCATION: $lat $lng")
        site.location.lat = lat
        site.location.lng = lng
        site.location.zoom = 15f

        map?.clear()
        map?.uiSettings?.isZoomControlsEnabled = true
        val options = MarkerOptions().title(site.name)
            .position(LatLng(site.location.lat, site.location.lng))
        map?.addMarker(options)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    site.location.lat,
                    site.location.lng
                ), site.location.zoom
            )
        )
        view?.showUpdatedMap(LatLng(site.location.lat, site.location.lng))
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                images.clear()
                if (data.clipData != null) {
                    if (data.clipData!!.itemCount > 4) {
                        view?.toast("Please select up to 4 images only")
                    } else {
                        val mClipData = data.clipData
                        var counter = 0
                        while (counter < mClipData!!.itemCount) {
                            val newImage = ImageModel()
                            newImage.uri = mClipData.getItemAt(counter).uri.toString()
                            newImage.fbID = site.fbId
                            newImage.id = Random().nextInt()
                            images.add(newImage)
                            counter++
                        }
                    }
                } else {
                    val newImage = ImageModel()
                    newImage.uri = data.data.toString()
                    newImage.fbID = site.fbId
                    newImage.id = Random().nextInt()
                    images.add(newImage)
                }

                site.images = images
                view?.showImages(images)
            }
            LOCATION_REQUEST -> {
                location = data.extras?.getParcelable("location")!!
                site.location = location
                locationUpdate(location.lat, location.lng)
            }
            IMAGE_CAPTURE_REQUEST -> {
                val path = getCurrentImagePath()
                if (path != null) {
                    if (site.images.size >= 4) {
                        view?.toast("Please select up to 4 images only")
                    } else {
                        val newImage = ImageModel()
                        newImage.uri = path
                        newImage.fbID = site.fbId
                        newImage.id = Random().nextInt()
                        images.add(newImage)

                        if (resultCode == Activity.RESULT_OK) {
                            site.images = images
                            view?.showImages(images)
                        }
                    }
                }
            }
        }
    }
}
