package com.archaeology.views.map

import com.archaeology.models.SiteModel
import com.archaeology.views.BasePresenter
import com.archaeology.views.BaseView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SiteMapPresenter(view: BaseView) : BasePresenter(view) {

    fun doPopulateMap(map: GoogleMap, Sites: List<SiteModel>) {
        map.uiSettings.setZoomControlsEnabled(true)
        Sites.forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }

    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        doAsync {
            val site = marker.tag as SiteModel
            uiThread {
                if (site != null) view?.showSite(site)
            }
        }
    }

    fun loadSites() {
        doAsync {
            val sites = app.sites.findAll()
            uiThread {
                view?.showSites(sites)
            }
        }
    }
}