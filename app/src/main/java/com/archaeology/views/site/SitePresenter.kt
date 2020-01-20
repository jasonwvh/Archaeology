package com.archaeology.views.site

import android.content.Intent
import com.archaeology.helpers.showImagePicker
import com.archaeology.models.Location
import com.archaeology.models.SiteModel
import com.archaeology.views.*


class SitePresenter(view: BaseView): BasePresenter(view) {

    var site = SiteModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;

    init {
        if (view.intent.hasExtra("site_edit")) {
            edit = true
            site = view.intent.extras?.getParcelable<SiteModel>("site_edit")!!
            view.showSite(site)
        }
    }

    fun doAddOrSave(title: String, description: String) {
        site.name = title
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
        if (edit == false) {
            view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", defaultLocation)
        } else {
            view?.navigateTo(
                VIEW.LOCATION,
                LOCATION_REQUEST,
                "location",
                Location(site.lat, site.lng, site.zoom)
            )
        }
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
            }
        }
    }
}