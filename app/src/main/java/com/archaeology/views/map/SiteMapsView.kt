package com.archaeology.views.map

import android.os.Bundle
import com.archaeology.R
import com.archaeology.models.SiteModel
import com.archaeology.views.BaseView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.content_site_maps.*
import kotlinx.android.synthetic.main.drawer_main.*

class SiteMapsView : BaseView(), GoogleMap.OnMarkerClickListener {

    private lateinit var presenter: SiteMapsPresenter
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content_frame.removeAllViews()
        layoutInflater.inflate(R.layout.activity_site_maps, content_frame)

        presenter = initPresenter(SiteMapsPresenter(this)) as SiteMapsPresenter

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadSites()
        }

        bottomNavBar.menu.findItem(R.id.navigation_map).isChecked = true
    }

    override fun showSite(site: SiteModel) {
        currentTitle.text = site.name
        currentRating.rating = site.rating.toFloat()
        if (site.images.isNotEmpty()) {
            Glide.with(currentImage.context).load(site.images[0].uri).centerCrop()
                .into(currentImage)
        } else {
            Glide.with(currentImage.context).load(R.drawable.placeholder).centerCrop()
                .into(currentImage)
        }
    }

    override fun showSites(sites: List<SiteModel>) {
        presenter.doPopulateMap(map, sites)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
