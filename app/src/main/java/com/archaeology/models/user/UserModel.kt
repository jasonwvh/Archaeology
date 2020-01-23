package com.archaeology.models.user

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.archaeology.models.site.SiteModel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var sites: List<SiteModel> = ArrayList()
) : Parcelable