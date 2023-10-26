package com.example.filmyapp.data_layer.local.models

import com.example.filmyapp.data_layer.local.database.entity.Movie
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("page")
    var page: Int? = null,

    @SerializedName("results")
    var results: List<Movie> = listOf(),

    @SerializedName("total_pages")
    var totalPages: Int? = null,

    @SerializedName("total_results")
    var totalResults: Int? = null
)