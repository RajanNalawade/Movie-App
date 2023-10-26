package com.example.filmyapp.data_layer.local.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.example.filmyapp.data_layer.local.models.Crew
import com.example.filmyapp.data_layer.local.models.Cast

sealed class CastCrew : Parcelable {
    @Parcelize
    data class CastData(val cast: Cast) : CastCrew()

    @Parcelize
    data class CrewData(val crew: Crew) : CastCrew()
}