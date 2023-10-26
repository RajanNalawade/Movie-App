package com.example.filmyapp.ui_layer.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.text.TextUtils
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.database.entity.Movie
import com.example.filmyapp.databinding.FragmentMoviesBinding
import com.example.filmyapp.ui_layer.adapters.MoviesAdapter
import com.example.filmyapp.ui_layer.details.MovieDetailsActivity
import com.example.filmyapp.ui_layer.search.SearchFragment
import com.example.filmyapp.ui_layer.search.SearchViewModel
import com.example.filmyapp.utility.FilmyUtility.getGridLayoutManager
import com.example.filmyapp.utility.PreferenceHelper.isDarkModeEnabled
import com.ferfalk.simplesearchview.SimpleSearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import showSnackBar

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var viewModelSearch: SearchViewModel
    private var adapterTrending: MoviesAdapter? = null
    private var adapterInTheaters: MoviesAdapter? = null
    private var adapterUpComing: MoviesAdapter? = null
    private var darkMode = false
    private lateinit var binding: FragmentMoviesBinding
    private var currentMovieType = MovieType.TRENDING

    enum class MovieType {
        TRENDING,
        IN_THEATERS,
        UPCOMING
    }

    companion object {
        const val MOVIE_ID = "MOVIE_ID"
        const val MOVIE_TITLE = "MOVIE_TITLE"
        const val FROM_ACTIVITY = "FROM_ACTIVITY"
        const val MOVIE_TYPE = "MOVIE_TYPE"
        const val DATABASE_APPLICABLE = "DATABASE_APPLICABLE"
        const val NETWORK_APPLICABLE = "NETWORK_APPLICABLE"
        const val SAVED_DATABASE_APPLICABLE = "SAVED_DATABASE_APPLICABLE"

        fun newInstance(movieType: MovieType): MoviesFragment {
            val args = Bundle()
            args.putSerializable(MOVIE_TYPE, movieType)
            val fragment = MoviesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        darkMode = isDarkModeEnabled(requireContext())
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        if (darkMode) darkThemeLogic() else lightThemeLogic()
        viewModelSearch = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        setupRecyclerViews()
        return binding.root
    }

    private fun setupRecyclerViews() {
        binding.recyclerTrending.layoutManager = getGridLayoutManager(requireActivity())
        binding.recyclerInTheaters.layoutManager = getGridLayoutManager(requireActivity())
        binding.recyclerUpComing.layoutManager = getGridLayoutManager(requireActivity())
        adapterTrending = MoviesAdapter { itemClicked(it) }
        adapterInTheaters = MoviesAdapter { itemClicked(it) }
        adapterUpComing = MoviesAdapter { itemClicked(it) }
        binding.recyclerTrending.adapter = adapterTrending
        //Restore RecyclerView scroll position
        binding.recyclerTrending.adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerInTheaters.adapter = adapterInTheaters
        //Restore RecyclerView scroll position
        binding.recyclerInTheaters.adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerUpComing.adapter = adapterUpComing
        //Restore RecyclerView scroll position
        binding.recyclerUpComing.adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearch()
        observerUiStates()
    }

    private fun observerUiStates() {
        lifecycleScope.launch {
            viewModel.uiStateTrending.collect {
                showMovies(it, MovieType.TRENDING)
            }
        }
        lifecycleScope.launch {
            viewModel.uiStateInTheaters.collect {
                showMovies(it, MovieType.IN_THEATERS)
            }
        }
        lifecycleScope.launch {
            viewModel.uiStateUpComing.collect {
                showMovies(it, MovieType.UPCOMING)
            }
        }

        lifecycleScope.launch {
            viewModelSearch.uiStateCloseSearch.collect {
                if (it) {
                    binding.searchView.closeSearch()
                    viewModelSearch.closeSearchDone()
                }
            }
        }
    }

    private fun showMovies(movies: List<Movie>, movieType: MovieType) {
        when (movieType) {
            MovieType.TRENDING -> adapterTrending?.swapData(movies)
            MovieType.IN_THEATERS -> adapterInTheaters?.swapData(movies)
            MovieType.UPCOMING -> adapterUpComing?.swapData(movies)
        }
    }

    private fun darkThemeLogic() {
        binding.logo.setTextColor(Color.parseColor("#E0E0E0"))
        binding.ivLogo.imageTintList =
            ContextCompat.getColorStateList(requireActivity(), R.color.colorMore)
        binding.searchView.setBackgroundColor(resources.getColor(R.color.colorDarkThemePrimary))
        binding.searchView.setBackIconDrawable(
            ContextCompat.getDrawable(
                requireContext(), com.ferfalk.simplesearchview.R.drawable.ic_arrow_back_black_24dp
            )
        ) /*setBackIcon(
            ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_action_navigation_arrow_back_inverted
            )
        )*/
        binding.searchView.setClearIconDrawable(
            ContextCompat.getDrawable(
                requireContext(), com.ferfalk.simplesearchview.R.drawable.ic_close_black_24dp
            )
        ) /*setCloseIcon(
            ContextCompat.getDrawable(
                requireContext(), R.drawable.ic_action_navigation_close_inverted
            )
        )*/
        binding.searchView.setTextColor(Color.parseColor("#ffffff"))
        binding.searchIcon.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.bg_search_icon_dark)
        TextViewCompat.setCompoundDrawableTintList(
            binding.searchIcon,
            ContextCompat.getColorStateList(requireActivity(), R.color.grey)
        )

        binding.searchView.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.fullBlack
            )
        )
    }

    private fun lightThemeLogic() {
        binding.logo.setTextColor(ContextCompat.getColor(requireActivity(), R.color.dark))
        binding.ivLogo.imageTintList =
            ContextCompat.getColorStateList(requireActivity(), R.color.colorMore)
        binding.searchIcon.background =
            ContextCompat.getDrawable(requireActivity(), R.drawable.bg_search_icon)
        TextViewCompat.setCompoundDrawableTintList(
            binding.searchIcon,
            ContextCompat.getColorStateList(requireActivity(), R.color.colorDarkThemePrimary)
        )
        binding.searchView.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorAccentTint
            )
        )
    }

    private fun setupSearch() {
        binding.searchIcon.setOnClickListener {
            binding.searchIcon.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            viewModelSearch.searchViewVisible()
            Handler().postDelayed({
                childFragmentManager.beginTransaction()
                    .replace(
                        R.id.search_container,
                        SearchFragment.newInstance(),
                        SearchFragment.TAG
                    )
                    .commit()
                binding.searchView.showSearch()
            }, 100)
        }

        binding.searchView.enableVoiceSearch(false)
        /*binding.searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                lifecycleScope.launch {
                    viewModelSearch.isSearchOpen.value = true
                }
            }

            override fun onSearchViewClosed() {
                val fragment = childFragmentManager.findFragmentByTag(SearchFragment.TAG)
                fragment?.let {
                    childFragmentManager
                        .beginTransaction()
                        .remove(it)
                        .commit()
                }
                viewModelSearch.searchViewHidden()

            }
        })*/
        binding.searchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                val fragment = childFragmentManager.findFragmentByTag(SearchFragment.TAG)
                fragment?.let {
                    childFragmentManager
                        .beginTransaction()
                        .remove(it)
                        .commit()
                }
                viewModelSearch.searchViewHidden()
            }

            override fun onSearchViewClosedAnimation() {
                binding.searchView.showSnackBar("onSearchViewShownAnimation", positive = false)
            }

            override fun onSearchViewShown() {
                lifecycleScope.launch {
                    viewModelSearch.isSearchOpen.value = true
                }
            }

            override fun onSearchViewShownAnimation() {
                binding.searchView.showSnackBar("onSearchViewShownAnimation", positive = false)
            }

        })


        // Emit Search Query
        lifecycleScope.launch {
            /*binding.searchView.setOnQueryTextListener(object :
                MaterialSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    val trimmedQuery = newText.trim { it <= ' ' }
                    val finalQuery = trimmedQuery.replace(" ", "-")
                    viewModelSearch.query.value = finalQuery
                    return true
                }
            })*/

            binding.searchView.setOnQueryTextListener(object :
                SimpleSearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    val trimmedQuery = newText.trim { it <= ' ' }
                    val finalQuery = trimmedQuery.replace(" ", "-")
                    viewModelSearch.query.value = finalQuery
                    return true
                }

                override fun onQueryTextCleared(): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onQueryTextSubmit(query: String): Boolean = true

            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SimpleSearchView.REQUEST_VOICE_SEARCH && resultCode == AppCompatActivity.RESULT_OK) {
            val matches = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null && matches.size > 0) {
                val searchWrd = matches[0]
                if (!TextUtils.isEmpty(searchWrd)) {
                    binding.searchView.setQuery(searchWrd, false)
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun itemClicked(movie: Movie) {
        Intent(activity, MovieDetailsActivity::class.java).run {
            putExtra(MOVIE_TITLE, movie.title)
            putExtra(FROM_ACTIVITY, true)
            putExtra(MOVIE_TYPE, currentMovieType)
            putExtra(DATABASE_APPLICABLE, true)
            putExtra(NETWORK_APPLICABLE, true)
            putExtra(MOVIE_ID, movie.id.toString())
            startActivity(this)
        }
        activity?.overridePendingTransition(0, 0)
    }
}