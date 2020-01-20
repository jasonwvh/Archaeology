package com.archaeology.models

interface SiteStore {
  fun findById(id: Long): SiteModel?
  fun findAll(): List<SiteModel>
  fun create(site: SiteModel)
  fun update(site: SiteModel)
  fun delete(site: SiteModel)
  fun clear()
}