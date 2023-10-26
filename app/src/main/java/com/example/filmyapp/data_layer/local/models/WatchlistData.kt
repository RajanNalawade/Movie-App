package com.example.filmyapp.data_layer.local.models

data class WatchlistData @JvmOverloads constructor(
    var id: String,
    var title: String? = null,
    var poster: String? = null
)