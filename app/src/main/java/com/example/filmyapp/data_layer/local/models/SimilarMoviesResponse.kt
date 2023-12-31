package com.example.filmyapp.data_layer.local.models

import com.google.gson.annotations.SerializedName

data class SimilarMoviesResponse(

    @SerializedName("page")
    var page: Int? = null,

    @SerializedName("results")
    var similars: ArrayList<SimilarMovie> = arrayListOf(),

    @SerializedName("total_pages")
    var totalPages: Int? = null,

    @SerializedName("total_results")
    var totalResults: Int? = null
)