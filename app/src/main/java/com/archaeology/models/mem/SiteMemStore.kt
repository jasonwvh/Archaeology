package com.archaeology.models.mem

import com.archaeology.models.SiteModel
import com.archaeology.models.SiteStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
  return lastId++
}

class SiteMemStore : SiteStore, AnkoLogger {

  val sites = ArrayList<SiteModel>()

  override fun findAll(): List<SiteModel> {
    return sites
  }

  override fun create(site: SiteModel) {
    site.id = getId()
    sites.add(site)
    logAll()
  }

  override fun update(site: SiteModel) {
    var foundSite: SiteModel? = sites.find { p -> p.id == site.id }
    if (foundSite != null) {
      foundSite.name = site.name
      foundSite.description = site.description
      foundSite.image = site.image
      foundSite.location = site.location
      logAll();
    }
  }

  override fun delete(site: SiteModel) {
    sites.remove(site)
  }

  override fun findById(id:Long) : SiteModel? {
    val foundSite: SiteModel? = sites.find { it.id == id }
    return foundSite
  }

  fun logAll() {
    sites.forEach { info("${it}") }
  }
}