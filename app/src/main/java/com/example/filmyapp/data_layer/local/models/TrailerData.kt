package com.example.filmyapp.data_layer.local.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrailerData(
    val title: String?,
    val url: String?
) : Parcelable