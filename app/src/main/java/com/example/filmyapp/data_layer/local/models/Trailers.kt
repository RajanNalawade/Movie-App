package com.example.filmyapp.data_layer.local.models

import com.google.gson.annotations.SerializedName

data class Trailers(

    @SerializedName("quicktime")
    var quicktime: ArrayList<String> = arrayListOf(),

    @SerializedName("youtube")
    var youtube: ArrayList<Youtube> = arrayListOf()
)