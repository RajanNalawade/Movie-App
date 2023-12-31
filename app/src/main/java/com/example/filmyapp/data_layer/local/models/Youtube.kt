package com.example.filmyapp.data_layer.local.models

import com.google.gson.annotations.SerializedName

data class Youtube(

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("size")
    var size: String? = null,

    @SerializedName("source")
    var source: String? = null,

    @SerializedName("type")
    var type: String? = null
)