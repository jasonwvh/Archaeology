package com.archaeology.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.archaeology.R
import com.archaeology.helpers.readImageFromPath
import com.archaeology.main.MainApp
import com.archaeology.models.SiteModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.android.synthetic.main.activity_site_maps.*
import kotlinx.android.synthetic.main.content_site_maps.*

class SiteMapsView : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    lateinit var presenter: SiteMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_maps)
        setSupportActionBar(toolbar)
        presenter = SiteMapPresenter(this)

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            presenter.doPopulateMap(it)
        }
    }

    fun showSite(site: SiteModel) {
        currentName.text = site.name
        currentDescription.text = site.description
        currentImage.setImageBitmap(readImageFromPath(this, site.image))
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
