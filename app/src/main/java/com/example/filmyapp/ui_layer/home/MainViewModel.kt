package com.example.filmyapp.ui_layer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmyapp.data_layer.local.database.entity.Movie
import com.example.filmyapp.di.FilmyApplication
import com.example.filmyapp.utility.KeystorePreference
import com.example.filmyapp.utility.KeystorePreference.OMDB_API_KEY
import com.example.filmyapp.utility.KeystorePreference.TMDB_API_KEY
import com.example.filmyapp.utility.KeystorePreference.YOUTUBE_API_KEY
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
class MainViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _uiStateTrending = MutableStateFlow(listOf<Movie>())
    private val _uiStateInTheaters = MutableStateFlow(listOf<Movie>())
    private val _uiStateUpcoming = MutableStateFlow(listOf<Movie>())

    val uiStateTrending: StateFlow<List<Movie>> = _uiStateTrending.asStateFlow()
    val uiStateInTheaters: StateFlow<List<Movie>> = _uiStateInTheaters.asStateFlow()
    val uiStateUpComing: StateFlow<List<Movie>> = _uiStateUpcoming.asStateFlow()

    init {
        //save all api keys to key store
        KeystorePreference.init(application = FilmyApplication.instance!!)
        if (KeystorePreference.get(TMDB_API_KEY).isNullOrEmpty())
            KeystorePreference.save(TMDB_API_KEY, "ad45629d1f8d387910cd4ccfbb72daea")
        if (KeystorePreference.get(OMDB_API_KEY).isNullOrEmpty())
            KeystorePreference.save(OMDB_API_KEY, "35758101")
        if (KeystorePreference.get(YOUTUBE_API_KEY).isNullOrEmpty())
            KeystorePreference.save(YOUTUBE_API_KEY, "AIzaSyBQqQL1dfVZV3KwF0bh_T_qeJZahTeHAUc")

        getTrending()
        getInTheaters()
        getUpComing()
    }

    private fun getTrending() {
        // Get Trending movies from local
        viewModelScope.launch(Dispatchers.IO) {
            val movies = moviesRepository.getTrendingFromLocal()
            if (movies.isNotEmpty()) {
                _uiStateTrending.emit(movies)
            }
        }

        // Get trending movies from network
        viewModelScope.launch {
            moviesRepository.getTrendingFromNetwork()
                .flowOn(Dispatchers.IO)
                .catch {
                    // Error
                    it.printStackTrace()
                }
                .collect {
                    val movies = it.results
                    if (movies.isNotEmpty()) {
                        _uiStateTrending.emit(movies)
                        // Update movies in DB
                        saveMoviesInDb(movies)
                    }
                }
        }
    }

    private fun getInTheaters() {
        // Get InTheaters movies from local
        viewModelScope.launch(Dispatchers.IO) {
            val movies = moviesRepository.getInTheatersFromLocal()
            if (movies.isNotEmpty()) {
                _uiStateInTheaters.emit(movies)
            }
        }

        // Get InTheaters movies from network
        viewModelScope.launch {
            moviesRepository.getInTheatersFromNetwork()
                .flowOn(Dispatchers.IO)
                .catch {
                    // Error
                    it.printStackTrace()
                }
                .collect {
                    val movies = it.results
                    if (movies.isNotEmpty()) {
                        _uiStateInTheaters.emit(movies)
                        // Update movies in DB
                        saveMoviesInDb(movies, 1)
                    }
                }
        }
    }

    private fun getUpComing() {
        // Get Upcoming movies from local
        viewModelScope.launch(Dispatchers.IO) {
            val movies = moviesRepository.getUpcomingFromLocal()
            if (movies.isNotEmpty()) {
                _uiStateUpcoming.emit(movies)
            }
        }

        // Get UpComing movies from network
        viewModelScope.launch {
            moviesRepository.getUpcomingFromNetwork()
                .flowOn(Dispatchers.IO)
                .catch {
                    // Error
                    it.printStackTrace()
                }
                .collect {
                    val movies = it.results
                    if (movies.isNotEmpty()) {
                        _uiStateUpcoming.emit(movies)
                        // Update movies in DB
                        saveMoviesInDb(movies, 2)
                    }
                }
        }
    }

    private fun saveMoviesInDb(movies: List<Movie>, type: Int = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            if (type != 0) {
                movies.forEach { movie ->
                    movie.type = type
                }
            }
            moviesRepository.addAllMoviesToDb(movies)
        }
    }
}