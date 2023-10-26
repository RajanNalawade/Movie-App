package com.example.filmyapp.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class FilmyWorkManager(private val context: Context) {

    fun createWork() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val workRequestTrending = PeriodicWorkRequest.Builder(
            TrendingWorker::class.java, SYNC_INTERVAL,
            TimeUnit.MILLISECONDS
        )
            .setConstraints(constraints)
            .build()

        val workRequestInTheaters = PeriodicWorkRequest.Builder(
            InTheatersWorker::class.java, SYNC_INTERVAL,
            TimeUnit.MILLISECONDS
        )
            .setConstraints(constraints)
            .build()

        val workRequestUpcoming = PeriodicWorkRequest.Builder(
            UpcomingWorker::class.java, SYNC_INTERVAL,
            TimeUnit.MILLISECONDS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "movie-trending-updates",
            ExistingPeriodicWorkPolicy.KEEP, workRequestTrending
        )

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "movie-intheaters-updates",
            ExistingPeriodicWorkPolicy.KEEP, workRequestInTheaters
        )

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "movie-upcoming-updates",
            ExistingPeriodicWorkPolicy.KEEP, workRequestUpcoming
        )
    }

    companion object {
        private const val SYNC_INTERVAL: Long = 21600000
    }
}