package com.example.filmyapp.utility

sealed class Secret(
    private val alias: String,
    private val data: String,
    private val iv: String
)

