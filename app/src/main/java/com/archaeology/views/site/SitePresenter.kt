package com.archaeology.views.site

import android.annotation.SuppressLint
import android.content.Intent
import com.archaeology.helpers.checkLocationPermissions
import com.archaeology.helpers.createDefaultLocationRequest
import com.archaeology.helpers.isPermissionGranted
import com.archaeology.helpers.showImagePicker
import com.archaeology.models.Location
import com.archaeology.models.SiteModel
import com.archaeology.views.*
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SitePresenter(view: BaseView) : BasePresenter(view) {

    var map: GoogleMap? = null
    var site = SiteModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;
    var locationService: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()

    init {
        if (view.intent.hasExtra("site_edit")) {
            edit = true
            site = view.intent.extras?.getParcelable<SiteModel>("site_edit")!!
            view.showSite(site)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }


    override fun doRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation.lat, defaultLocation.lng)
        }
    }

    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(site.lat, site.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        site.lat = lat
        site.lng = lng
        site.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(site.name).position(LatLng(site.lat, site.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(site.lat, site.lng), site.zoom))
        view?.showSite(site)
    }


    fun doAddOrSave(name: String, description: String) {
        site.name = name
        site.description = description
        if (edit) {
            app.sites.update(site)
        } else {
            app.sites.create(site)
        }
        view?.finish()
    }

    fun doCancel() {
        view?.finish()
    }

    fun doDelete() {
        app.sites.delete(site)
        view?.finish()
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
            Location(site.lat, site.lng, site.zoom)
        )
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                site.image = data.data.toString()
                view?.showSite(site)
            }
            LOCATION_REQUEST -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                site.lat = location.lat
                site.lng = location.lng
                site.zoom = location.zoom
                locationUpdate(site.lat, site.lng)
            }
        }
    }
}
