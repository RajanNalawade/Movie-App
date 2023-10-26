package com.example.filmyapp.ui_layer.cast_crew

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.models.CastCrewDetailsResponse
import com.example.filmyapp.data_layer.local.models.CastCrewMoviesResponse
import com.example.filmyapp.data_layer.local.models.CastMovie
import com.example.filmyapp.databinding.ActivityDetailedCastBinding
import com.example.filmyapp.ui_layer.adapters.MemberMoviesAdapter
import com.example.filmyapp.ui_layer.details.MovieDetailsActivity
import com.example.filmyapp.ui_layer.full.AllMoviesActivity
import com.example.filmyapp.ui_layer.full.FullReadFragment
import com.example.filmyapp.ui_layer.home.MoviesFragment
import com.example.filmyapp.utility.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import toReadableDate

@AndroidEntryPoint
class CastCrewDetailsActivity : AppCompatActivity() {

    private val viewModel: CastCrewViewModel by viewModels()
    private var characterId: String? = null
    private var characterTitle: String? = null
    private var moviesList: ArrayList<CastMovie>? = null
    private var characterBio: String? = null
    private var nightMode = false
    private lateinit var binding: ActivityDetailedCastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        nightMode = PreferenceHelper.isDarkModeEnabled(this)
        if (nightMode) setTheme(R.style.AppTheme_MD3_Dark) else setTheme(R.style.AppTheme_MD3)

        super.onCreate(savedInstanceState)
        binding = ActivityDetailedCastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.more.setOnClickListener {
            if (!(moviesList == null && characterTitle == null)) {
                val intent = Intent(this@CastCrewDetailsActivity, AllMoviesActivity::class.java)
                intent.putExtra(CastCrewFragment.MOVIES, moviesList)
                intent.putExtra(CastCrewFragment.TOOLBAR_TITLE, characterTitle)
                startActivity(intent)
            }
        }

        binding.characterMovies.layoutManager = GridLayoutManager(this@CastCrewDetailsActivity, 3)
        binding.characterMovies.isNestedScrollingEnabled = false

        binding.overviewContainer.setOnClickListener {
            if (characterTitle != null && characterBio != null) {
                val fragment = FullReadFragment.newInstance(characterTitle, characterBio)
                fragment.show(supportFragmentManager, FullReadFragment.DESCRIPTION)
            }
        }

        characterId = intent?.getStringExtra(CastCrewFragment.MEMBER_ID)
        observeUiState()

        characterId?.let {
            viewModel.getCastCrewDetails(it)
            viewModel.getCastCrewMovies(it)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiStateCastCrewDetails.collect {
                it?.let {
                    showPersonalDetails(it)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.uiStateCastCrewMovies.collect {
                it?.let {
                    moviesList = it.castMovies
                    showPersonMovies(it)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (nightMode != PreferenceHelper.isDarkModeEnabled(this)) recreate()
    }

    private fun showPersonalDetails(details: CastCrewDetailsResponse) {
        characterTitle = details.name
        characterBio = details.biography
        binding.name.text = details.name

        details.birthday?.let {
            binding.dob.text = it.toReadableDate()
        } ?: run {
            binding.dob.visibility = View.GONE
        }

        details.placeOfBirth?.let {
            binding.birthPlace.text = it
        } ?: run {
            binding.birthPlace.visibility = View.GONE
        }

        if (details.biography?.isEmpty() == true) {
            binding.overviewContainer.visibility = View.GONE
            binding.overview.visibility = View.GONE
        } else {
            if (Build.VERSION.SDK_INT >= 24) {
                binding.overview.text = Html.fromHtml(details.biography, Html.FROM_HTML_MODE_LEGACY)
            }
        }

        Glide.with(this)
            .load(getString(R.string.member_profile_url_2, details.profilePath))
            .fitCenter().into(binding.displayProfile)
    }

    private fun showPersonMovies(castMovieDetails: CastCrewMoviesResponse) {
        val charAdapter =
            MemberMoviesAdapter(castMovieDetails.castMovies, true) { movie, _ ->
                val intent = Intent(this, MovieDetailsActivity::class.java)
                intent.putExtra(MoviesFragment.MOVIE_ID, movie.id.toString())
                intent.putExtra(MoviesFragment.MOVIE_TITLE, movie.title)
                intent.putExtra(MoviesFragment.NETWORK_APPLICABLE, true)
                intent.putExtra(MoviesFragment.FROM_ACTIVITY, false)
                startActivity(intent)
            }
        charAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.characterMovies.adapter = charAdapter
        if (castMovieDetails.castMovies.size > 5) {
            binding.more.visibility = View.VISIBLE
        } else {
            binding.more.visibility = View.INVISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentByTag(FullReadFragment.DESCRIPTION) as FullReadFragment?
        if (fragment != null && fragment.isVisible) {
            fragment.dismiss()
        } else {

            onBackPressedDispatcher.onBackPressed()
        }
    }
}