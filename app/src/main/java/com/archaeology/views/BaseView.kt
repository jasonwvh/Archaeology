package com.archaeology.views

import android.app.ActivityOptions
import android.content.Intent
import android.os.Parcelable
import com.archaeology.models.site.ImageModel
import com.archaeology.models.site.NoteModel
import com.archaeology.models.site.SiteModel
import com.archaeology.views.editlocation.EditLocationView
import com.archaeology.views.home.HomeView
import com.archaeology.views.login.LoginView
import com.archaeology.views.map.SiteMapsView
import com.archaeology.views.signup.SignUpView
import com.archaeology.views.site.SiteView
import com.archaeology.views.sitelist.SiteListView
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.AnkoLogger

enum class VIEW {
    LOCATION, SITE, MAPS, LIST, SIGNUP, HOME, LOGIN
}

abstract class BaseView : HomeView(), AnkoLogger {

    private var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        val intent: Intent = when (view) {
            VIEW.LOCATION -> Intent(this, EditLocationView::class.java)
            VIEW.SITE -> Intent(this, SiteView::class.java)
            VIEW.MAPS -> Intent(this, SiteMapsView::class.java)
            VIEW.LIST -> Intent(this, SiteListView::class.java)
            VIEW.SIGNUP -> Intent(this, SignUpView::class.java)
            VIEW.HOME -> Intent(this, HomeView::class.java)
            VIEW.LOGIN -> Intent(this, LoginView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }

        startActivityForResult(
            intent,
            code,
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showSite(site: SiteModel) {}
    open fun showSites(sites: List<SiteModel>) {}
    open fun showNotes(notes: ArrayList<NoteModel>?) {}
    open fun showImages(images: ArrayList<ImageModel>) {}
    open fun showUpdatedMap(latLng: LatLng) {}
    open fun showProgress() {}
    open fun hideProgress() {}
    open fun showAccount(user: FirebaseUser) {}

}