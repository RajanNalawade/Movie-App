package com.example.filmyapp.utility

data class DecryptionSecret(
    val alias: String,
    val data: String,
    val iv: String
)
