package com.archaeology.models

interface SiteStore {
  fun findAllSites(): List<SiteModel>?
  fun findOneSite(siteID: Int): SiteModel?
  fun findSitesByName(name: String): ArrayList<SiteModel>?
  fun createSite(site: SiteModel)
  fun updateSite(site: SiteModel)
  fun deleteSite(site: SiteModel)
  fun toggleFavourite(site: SiteModel)
  fun sortedByFavourite(): List<SiteModel>?
  fun sortByRating(): List<SiteModel>?
  fun sortByVisit(): List<SiteModel>?
}