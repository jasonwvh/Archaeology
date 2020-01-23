package com.archaeology.views.map

import com.archaeology.models.SiteModel
import com.archaeology.views.BasePresenter
import com.archaeology.views.BaseView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class SiteMapsPresenter(view: BaseView) : BasePresenter(view) {

    fun doPopulateMap(map: GoogleMap, sites: List<SiteModel>) {
        map.uiSettings.isZoomControlsEnabled = true
        if (sites.isNotEmpty()) {
            sites.forEach {
                val loc = LatLng(it.location.lat, it.location.lng)
                val options = MarkerOptions().title(it.name).position(loc)
                map.addMarker(options).tag = it
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
            }
            view?.showSite(sites[0])
        }
    }

    fun doMarkerSelected(marker: Marker) {
        val site = marker.tag as SiteModel
        view?.showSite(site)
    }

    fun loadSites() {
        view?.showSites(app.sites.findAllSites()!!)
    }
}