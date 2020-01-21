package com.archaeology.views.sitelist

import com.archaeology.models.SiteModel
import com.archaeology.views.BasePresenter
import com.archaeology.views.BaseView
import com.archaeology.views.VIEW
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SiteListPresenter(view: BaseView): BasePresenter(view) {

    fun doAddSite() {
        view?.navigateTo(VIEW.SITE)
    }

    fun doEditSite(site: SiteModel) {
        view?.navigateTo(VIEW.SITE, 0, "site_edit", site)
    }

    fun doShowSitesMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun loadSites() {
        doAsync {
            val sites = app.sites.findAll()
            uiThread {
                view?.showSites(sites)
            }
        }
    }

    fun doLogout() {
        FirebaseAuth.getInstance().signOut()
        view?.navigateTo(VIEW.LOGIN)
    }
}