package com.example.filmyapp.data_layer.local.models

import com.google.gson.annotations.SerializedName

data class CastCrewMoviesResponse(
    @SerializedName("cast")
    var castMovies: ArrayList<CastMovie> = arrayListOf(),

    @SerializedName("crew")
    var crewMovies: ArrayList<CrewMovie> = arrayListOf(),

    @SerializedName("id")
    var id: Int? = null
)