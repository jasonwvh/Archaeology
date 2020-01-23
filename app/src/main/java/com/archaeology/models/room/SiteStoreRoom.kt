package com.archaeology.models.room

/*
import android.content.Context
import androidx.room.Room
import com.archaeology.models.SiteModel
import com.archaeology.models.SiteStore

class SiteStoreRoom(val context: Context) : SiteStore {

    var dao: SiteDAO

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.siteDAO()
    }

    override fun findAll(): List<SiteModel> {
        return dao.findAll()
    }

    override fun findById(id: Long): SiteModel? {
        return dao.findById(id)
    }

    override fun create(site: SiteModel) {
        dao.create(site)
    }

    override fun update(site: SiteModel) {
        dao.update(site)
    }

    override fun delete(site: SiteModel) {
        dao.deleteSite(site)
    }
}
 */