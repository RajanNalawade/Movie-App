package com.example.filmyapp.ui_layer.cast_crew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmyapp.data_layer.local.models.CastAndCrewResponse
import com.example.filmyapp.data_layer.local.models.CastCrewDetailsResponse
import com.example.filmyapp.data_layer.local.models.CastCrewMoviesResponse
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
class CastCrewViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _uiStateCastAndCrew = MutableStateFlow<CastAndCrewResponse?>(null)
    private val _uiStateCastCrewDetails = MutableStateFlow<CastCrewDetailsResponse?>(null)
    private val _uiStateCastCrewMovies = MutableStateFlow<CastCrewMoviesResponse?>(null)
    val uiStateCastAndCrew: StateFlow<CastAndCrewResponse?> = _uiStateCastAndCrew.asStateFlow()
    val uiStateCastCrewDetails: StateFlow<CastCrewDetailsResponse?> =
        _uiStateCastCrewDetails.asStateFlow()
    val uiStateCastCrewMovies: StateFlow<CastCrewMoviesResponse?> =
        _uiStateCastCrewMovies.asStateFlow()

    fun getCastAndCrew(movieId: String) {
        viewModelScope.launch {
            moviesRepository.getCastAndCrew(movieId)
                .flowOn(Dispatchers.IO)
                .catch {
                    it.printStackTrace()
                }.collect { castAndCrew ->
                    _uiStateCastAndCrew.emit(castAndCrew)
                }
        }
    }

    fun getCastCrewDetails(memberId: String) {
        viewModelScope.launch {
            moviesRepository.getCastCrewDetails(memberId)
                .flowOn(Dispatchers.IO)
                .catch {
                    it.printStackTrace()
                }.collect { castCrewDetails ->
                    _uiStateCastCrewDetails.emit(castCrewDetails)
                }
        }
    }

    fun getCastCrewMovies(memberId: String) {
        viewModelScope.launch {
            moviesRepository.getCastCrewMovies(memberId)
                .flowOn(Dispatchers.IO)
                .catch {
                    it.printStackTrace()
                }.collect { castCrewMovies ->
                    _uiStateCastCrewMovies.emit(castCrewMovies)
                }
        }
    }
}