package com.example.filmyapp.data_layer.remote

import com.example.filmyapp.data_layer.local.database.entity.MovieDetails
import com.example.filmyapp.data_layer.local.models.CastAndCrewResponse
import com.example.filmyapp.data_layer.local.models.CastCrewDetailsResponse
import com.example.filmyapp.data_layer.local.models.CastCrewMoviesResponse
import com.example.filmyapp.data_layer.local.models.MoviesResponse
import com.example.filmyapp.data_layer.local.models.RatingResponse
import com.example.filmyapp.data_layer.local.models.SearchResultResponse
import com.example.filmyapp.data_layer.local.models.SimilarMoviesResponse
import kotlinx.coroutines.flow.Flow

interface MoviesApiHelper {
    fun getTrending(): Flow<MoviesResponse>
    fun getUpcoming(): Flow<MoviesResponse>
    fun getInTheaters(): Flow<MoviesResponse>
    fun getMovieDetails(id: String): Flow<MovieDetails>
    fun getOMDBRatings(id: String): Flow<RatingResponse>
    fun getCastAndCew(id: String): Flow<CastAndCrewResponse>
    fun getSimilar(id: String): Flow<SimilarMoviesResponse>
    fun getCastCrewDetails(id: String): Flow<CastCrewDetailsResponse>
    fun getCastCrewMovies(id: String): Flow<CastCrewMoviesResponse>
    fun searchMovies(query: String): Flow<SearchResultResponse>
}