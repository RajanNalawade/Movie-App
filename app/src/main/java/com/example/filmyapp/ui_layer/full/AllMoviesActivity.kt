package com.example.filmyapp.ui_layer.full

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.models.CastMovie
import com.example.filmyapp.databinding.ActivityFullMovieBinding
import com.example.filmyapp.ui_layer.adapters.MemberMoviesAdapter
import com.example.filmyapp.ui_layer.cast_crew.CastCrewFragment.Companion.MOVIES
import com.example.filmyapp.ui_layer.cast_crew.CastCrewFragment.Companion.TOOLBAR_TITLE
import com.example.filmyapp.ui_layer.details.MovieDetailsActivity
import com.example.filmyapp.ui_layer.home.MoviesFragment.Companion.FROM_ACTIVITY
import com.example.filmyapp.ui_layer.home.MoviesFragment.Companion.MOVIE_ID
import com.example.filmyapp.ui_layer.home.MoviesFragment.Companion.MOVIE_TITLE
import com.example.filmyapp.ui_layer.home.MoviesFragment.Companion.NETWORK_APPLICABLE
import com.example.filmyapp.utility.PreferenceHelper

class AllMoviesActivity : AppCompatActivity() {

    private var movieList: List<CastMovie>? = null
    private var nightMode = false
    private lateinit var binding: ActivityFullMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        nightMode = PreferenceHelper.isDarkModeEnabled(this)
        if (nightMode) setTheme(R.style.AppTheme_MD3_Dark) else setTheme(R.style.AppTheme_MD3)

        super.onCreate(savedInstanceState)
        binding = ActivityFullMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.fullMovieRecycler.layoutManager = GridLayoutManager(this@AllMoviesActivity, 3)
        movieList = intent?.getSerializableExtra(MOVIES) as List<CastMovie>

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
        binding.title.text = intent?.getStringExtra(TOOLBAR_TITLE)

        movieList?.let {
            val movieAdapter =
                MemberMoviesAdapter(it, false) { movie, _ ->
                    val intent = Intent(this, MovieDetailsActivity::class.java)
                    intent.putExtra(MOVIE_ID, movie.id.toString())
                    intent.putExtra(MOVIE_TITLE, movie.title)
                    intent.putExtra(NETWORK_APPLICABLE, true)
                    intent.putExtra(FROM_ACTIVITY, false)
                    startActivity(intent)
                }
            movieAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            binding.fullMovieRecycler.adapter = movieAdapter
        }

        if (nightMode) allThemeLogic()
    }

    private fun allThemeLogic() {
        binding.title.setTextColor(Color.parseColor("#bdbdbd"))
    }

    override fun onResume() {
        super.onResume()
        if (nightMode != PreferenceHelper.isDarkModeEnabled(this)) recreate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}