package com.example.filmyapp.data_layer.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.filmyapp.data_layer.local.database.dao.MovieDao
import com.example.filmyapp.data_layer.local.database.dao.MovieDetailsDao
import com.example.filmyapp.data_layer.local.database.entity.Movie
import com.example.filmyapp.data_layer.local.database.entity.MovieDetails

@Database(entities = [Movie::class, MovieDetails::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class FilmyDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun movieDetailsDao(): MovieDetailsDao
}