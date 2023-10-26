package com.example.filmyapp.ui_layer.similar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmyapp.data_layer.local.models.SimilarMoviesResponse
import com.example.filmyapp.ui_layer.home.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimilarViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _uiStateSimilar = MutableStateFlow<SimilarMoviesResponse?>(null)
    val uiStateSimilar: StateFlow<SimilarMoviesResponse?> = _uiStateSimilar.asStateFlow()

    fun getSimilar(movieId: String) {
        viewModelScope.launch {
            moviesRepository.getSimilar(movieId)
                .flowOn(Dispatchers.IO)
                .catch {
                    // error
                }.collect { similarResponse ->
                    _uiStateSimilar.emit(similarResponse)
                }
        }
    }
}