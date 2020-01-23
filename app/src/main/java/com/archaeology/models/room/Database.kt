package com.archaeology.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.archaeology.models.SiteModel

@Database(entities = arrayOf(SiteModel::class), version = 1,  exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun siteDAO(): SiteDAO
}