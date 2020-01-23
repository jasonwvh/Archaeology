package com.archaeology.views.sitelist

import com.archaeology.models.SiteModel
import com.archaeology.views.BasePresenter
import com.archaeology.views.BaseView
import com.archaeology.views.VIEW
import com.archaeology.views.site.SiteView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.uiThread

class SiteListPresenter(view: BaseView): BasePresenter(view) {

    private var currentSites: List<SiteModel> = arrayListOf()

    fun doAddSite() {
        view?.startActivityForResult<SiteView>(0)
    }

    fun doEditSite(site: SiteModel) {
        view?.navigateTo(VIEW.SITE, 0, "site_edit", site)
    }

    fun loadSites() {
        doAsync {
            val sites = app.sites.findAllSites()
            view?.info(sites)
            uiThread {
                if (sites != null) {
                    currentSites = sites
                    view?.showSites(sites)
                }
            }
        }
    }

    fun doSortByRating() {
        val ratedSites = app.sites.sortByRating()
        if (ratedSites != null) {
            currentSites = ratedSites
            view?.showSites(ratedSites)
        }
    }


    fun doAscendingOrder() {
        view?.showSites(currentSites)
    }

    fun doDescendingOrder() {
        view?.showSites(currentSites.asReversed())
    }
}