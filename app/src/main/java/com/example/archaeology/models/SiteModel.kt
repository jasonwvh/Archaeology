package com.example.archaeology.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SiteModel(var id: Long = 0,
                          var title: String = "",
                          var description: String = "") : Parcelable

