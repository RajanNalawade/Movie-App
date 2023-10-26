package com.example.filmyapp.di

import android.content.Context
import androidx.room.Room
import com.example.filmyapp.data_layer.local.database.FilmyDatabase
import com.example.filmyapp.data_layer.remote.MoviesApiHelper
import com.example.filmyapp.data_layer.remote.MoviesApiHelperImpl
import com.example.filmyapp.data_layer.remote.MoviesApiService
import com.example.filmyapp.utility.KeystorePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object MoviesModule {

    @Provides
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val original = it.request()
            val originalHttpUrl = original.url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter(
                    "api_key",
                    KeystorePreference.get(KeystorePreference.TMDB_API_KEY)
                )
                .build()
            val requestBuilder = original.newBuilder().url(url)
            val request = requestBuilder.build()
            it.proceed(request)
        }.build()

    @Provides
    fun providesRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(MoviesApiService.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideMoviesServices(retrofit: Retrofit): MoviesApiService =
        retrofit.create(MoviesApiService::class.java)

    @Provides
    fun provideMoviesApiHelper(apiService: MoviesApiService): MoviesApiHelper =
        MoviesApiHelperImpl(apiService)

    @Provides
    fun providesMoviesDatabase(@ApplicationContext appContext: Context): FilmyDatabase {
        return Room.databaseBuilder(
            context = appContext,
            klass = FilmyDatabase::class.java,
            name = "MovieApp"
        ).build()
    }

}