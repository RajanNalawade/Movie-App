package com.example.filmyapp.data_layer.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.filmyapp.data_layer.local.database.entity.MovieDetails


@Dao
interface MovieDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieDetails: MovieDetails)

    @Delete
    fun delete(movie: MovieDetails)

    @Query("SELECT * FROM movie_details")
    fun getAllDetails(): List<MovieDetails>

    @Query("SELECT * FROM movie_details WHERE favorite = 1")
    fun getAllFavorites(): List<MovieDetails>

    @Query("SELECT * FROM movie_details WHERE watchlist = 1")
    fun getAllWatchlist(): List<MovieDetails>

    @Query("SELECT * FROM movie_details WHERE id = :id AND type = :type")
    fun getDetailsOfType(id: Int, type: Int = 0): MovieDetails?

    @Update
    fun updateDetails(movie: MovieDetails): Int
}