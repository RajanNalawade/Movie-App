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
import kotlinx.coroutines.flow.flow

class MoviesApiHelperImpl(private val apiService: MoviesApiService) : MoviesApiHelper {

    override fun getTrending(): Flow<MoviesResponse> = flow {
        emit(apiService.getTrending())
    }

    override fun getInTheaters(): Flow<MoviesResponse> = flow {
        emit(apiService.getInTheaters())
    }

    override fun getUpcoming(): Flow<MoviesResponse> = flow {
        emit(apiService.getUpcoming())
    }

    override fun getMovieDetails(id: String): Flow<MovieDetails> = flow {
        emit(apiService.getMovieDetails(id))
    }

    override fun getOMDBRatings(id: String): Flow<RatingResponse> = flow {
        emit(
            apiService.getOMDBRatings(
                MoviesApiService.BASE_URL_OMDB,
                id,
                "OMDB_API_KEY",
                true,
                "json"
            )
        )
    }

    override fun getCastAndCew(id: String): Flow<CastAndCrewResponse> = flow {
        emit(apiService.getCasts(id))
    }

    override fun getSimilar(id: String): Flow<SimilarMoviesResponse> = flow {
        emit(apiService.getSimilarMovies(id))
    }

    override fun getCastCrewDetails(id: String): Flow<CastCrewDetailsResponse> = flow {
        emit(apiService.getCastCrewDetails(id))
    }

    override fun getCastCrewMovies(id: String): Flow<CastCrewMoviesResponse> = flow {
        emit(apiService.getCastCrewMovies(id))
    }

    override fun searchMovies(query: String): Flow<SearchResultResponse> = flow {
        emit(apiService.searchMovies(query))
    }
}