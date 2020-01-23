package com.archaeology.models.room


import androidx.room.*
import com.archaeology.models.SiteModel

@Dao
interface SiteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(site: SiteModel)

    @Query("SELECT * FROM SiteModel")
    fun findAll(): List<SiteModel>

    @Query("select * from SiteModel where id = :id")
    fun findById(id: Long): SiteModel

    @Update
    fun update(site: SiteModel)

    @Delete
    fun deleteSite(site: SiteModel)
}