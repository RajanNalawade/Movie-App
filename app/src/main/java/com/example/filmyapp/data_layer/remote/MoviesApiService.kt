package com.example.filmyapp.data_layer.remote

import com.example.filmyapp.data_layer.local.database.entity.MovieDetails
import com.example.filmyapp.data_layer.local.models.CastAndCrewResponse
import com.example.filmyapp.data_layer.local.models.CastCrewDetailsResponse
import com.example.filmyapp.data_layer.local.models.CastCrewMoviesResponse
import com.example.filmyapp.data_layer.local.models.MoviesResponse
import com.example.filmyapp.data_layer.local.models.RatingResponse
import com.example.filmyapp.data_layer.local.models.SearchResultResponse
import com.example.filmyapp.data_layer.local.models.SimilarMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface MoviesApiService {

    companion object {
        var BASE_URL = "https://api.themoviedb.org/3/"
        var BASE_URL_OMDB = "http://www.omdbapi.com/"
    }

    @GET("movie/popular")
    suspend fun getTrending(): MoviesResponse

    @GET("movie/now_playing")
    suspend fun getInTheaters(): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpcoming(): MoviesResponse

    @GET("movie/{movie_id}?append_to_response=trailers")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String?): MovieDetails

    @GET
    suspend fun getOMDBRatings(
        @Url url: String,
        @Query("i") movieId: String,
        @Query("apikey") apiKey: String,
        @Query("tomatoes") tomatoes: Boolean,
        @Query("r") r: String,
    ): RatingResponse

    @GET("movie/{movie_id}/casts")
    suspend fun getCasts(@Path("movie_id") movieId: String): CastAndCrewResponse

    @GET("movie/{movie_id}/recommendations")
    suspend fun getSimilarMovies(@Path("movie_id") movieId: String): SimilarMoviesResponse

    @GET("person/{person_id}")
    suspend fun getCastCrewDetails(@Path("person_id") personId: String): CastCrewDetailsResponse

    @GET("person/{person_id}/movie_credits")
    suspend fun getCastCrewMovies(@Path("person_id") personId: String): CastCrewMoviesResponse

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): SearchResultResponse
}