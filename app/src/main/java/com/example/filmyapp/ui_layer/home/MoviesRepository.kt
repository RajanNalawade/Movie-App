package com.example.filmyapp.ui_layer.home

import com.example.filmyapp.data_layer.local.database.FilmyDatabase
import com.example.filmyapp.data_layer.local.database.entity.Movie
import com.example.filmyapp.data_layer.local.database.entity.MovieDetails
import com.example.filmyapp.data_layer.local.models.CastAndCrewResponse
import com.example.filmyapp.data_layer.local.models.CastCrewDetailsResponse
import com.example.filmyapp.data_layer.local.models.CastCrewMoviesResponse
import com.example.filmyapp.data_layer.local.models.MoviesResponse
import com.example.filmyapp.data_layer.local.models.RatingResponse
import com.example.filmyapp.data_layer.local.models.SearchResultResponse
import com.example.filmyapp.data_layer.local.models.SimilarMoviesResponse
import com.example.filmyapp.data_layer.remote.MoviesApiHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val filmyDatabase: FilmyDatabase,
    private val moviesApiHelper: MoviesApiHelper
) {

    fun getTrendingFromLocal(): List<Movie> = filmyDatabase.movieDao().getAllTrending()

    fun getTrendingFromNetwork(): Flow<MoviesResponse> = moviesApiHelper.getTrending()

    fun getUpcomingFromLocal(): List<Movie> = filmyDatabase.movieDao().getAllUpcoming()

    fun getUpcomingFromNetwork(): Flow<MoviesResponse> = moviesApiHelper.getUpcoming()

    fun getInTheatersFromLocal(): List<Movie> = filmyDatabase.movieDao().getAllInTheaters()

    fun getInTheatersFromNetwork(): Flow<MoviesResponse> = moviesApiHelper.getInTheaters()

    fun getMovieDetailsFromLocal(id: Int, type: Int): MovieDetails? {
        return filmyDatabase.movieDetailsDao().getDetailsOfType(id, type)
    }

    fun getRatings(id: String): Flow<RatingResponse> = moviesApiHelper.getOMDBRatings(id)

    fun getMovieDetailsFromNetwork(id: String): Flow<MovieDetails> {
        return moviesApiHelper.getMovieDetails(id)
    }

    fun getCastAndCrew(id: String): Flow<CastAndCrewResponse> = moviesApiHelper.getCastAndCew(id)

    fun getCastCrewDetails(id: String): Flow<CastCrewDetailsResponse> =
        moviesApiHelper.getCastCrewDetails(id)

    fun getCastCrewMovies(id: String): Flow<CastCrewMoviesResponse> =
        moviesApiHelper.getCastCrewMovies(id)

    fun getSimilar(id: String): Flow<SimilarMoviesResponse> = moviesApiHelper.getSimilar(id)

    fun searchMovies(query: String): Flow<SearchResultResponse> =
        moviesApiHelper.searchMovies(query)

    fun addMovieDetailsToLocal(movieDetails: MovieDetails) {
        return filmyDatabase.movieDetailsDao().insert(movieDetails)
    }

    fun getFavorites(): List<MovieDetails> = filmyDatabase.movieDetailsDao().getAllFavorites()

    fun getWatchlist(): List<MovieDetails> = filmyDatabase.movieDetailsDao().getAllWatchlist()

    fun updateMovieDetails(movieDetails: MovieDetails): Int =
        filmyDatabase.movieDetailsDao().updateDetails(movieDetails)

    fun addAllMoviesToDb(movies: List<Movie>) {
        filmyDatabase.movieDao().insertAll(movies)
    }
}