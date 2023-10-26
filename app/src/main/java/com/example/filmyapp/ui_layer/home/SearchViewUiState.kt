package com.example.filmyapp.ui_layer.home

sealed class SearchViewUiState {
    object Hidden : SearchViewUiState()
    object Visible : SearchViewUiState()
}
