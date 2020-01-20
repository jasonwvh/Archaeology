package com.archaeology.views.map

import com.archaeology.models.SiteModel
import com.archaeology.views.BasePresenter
import com.archaeology.views.BaseView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class SiteMapPresenter(view: BaseView) : BasePresenter(view) {

    fun doPopulateMap(map: GoogleMap, Sites: List<SiteModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        Sites.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        val Site = app.sites.findById(tag)
        if (Site != null) view?.showSite(Site)

    }

    fun loadSites() {
        view?.showSites(app.sites.findAll())
    }
}