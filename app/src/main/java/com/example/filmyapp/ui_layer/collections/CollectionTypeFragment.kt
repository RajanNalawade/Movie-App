package com.example.filmyapp.ui_layer.collections

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.database.entity.MovieDetails
import com.example.filmyapp.databinding.FragmentCollectionMoviesBinding
import com.example.filmyapp.ui_layer.adapters.CollectionsAdapter
import com.example.filmyapp.ui_layer.details.MovieDetailsActivity
import com.example.filmyapp.ui_layer.home.MoviesFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionTypeFragment : Fragment() {

    private val viewModel: CollectionsViewModel by viewModels()
    private var adapter: CollectionsAdapter? = null
    private var _binding: FragmentCollectionMoviesBinding? = null
    private val binding get() = _binding!!
    private var currentCollectionType = CollectionType.FAVORITE

    enum class CollectionType {
        FAVORITE,
        WATCHLIST
    }

    companion object {
        const val COLLECTION_TYPE = "COLLECTION_TYPE"
        fun newInstance(collectionType: CollectionType): CollectionTypeFragment {
            val args = Bundle()
            args.putSerializable(COLLECTION_TYPE, collectionType)
            val fragment = CollectionTypeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionMoviesBinding.inflate(inflater, container, false)
        val view = binding.root

        currentCollectionType = arguments?.getSerializable(COLLECTION_TYPE) as CollectionType
        val spanCount = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 3
            else -> 5
        }
        binding.mySavedRecycler.layoutManager = GridLayoutManager(context, spanCount)
        adapter = CollectionsAdapter(clickListener = { movieId, title ->
            itemClicked(movieId, title)
        }) { dataCursor, position ->
            itemLongClicked(dataCursor, position)
        }
        adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.mySavedRecycler.adapter = adapter
        observeUiStates()
        return view
    }

    override fun onResume() {
        super.onResume()

        viewModel.getFavorites()
        viewModel.getWatchLists()
    }

    private fun observeUiStates() {
        lifecycleScope.launch {
            when (currentCollectionType) {
                CollectionType.FAVORITE -> {
                    viewModel.uiStateFavorites.collect {
                        it?.let {
                            showMovies(it)
                            binding.emptyContainer.isVisible = false
                        } ?: run {
                            binding.emptyContainer.isVisible = true
                        }
                    }
                }

                CollectionType.WATCHLIST -> {
                    viewModel.uiStateWatchlist.collect {
                        it?.let {
                            showMovies(it)
                            binding.emptyContainerWatch.isVisible = false
                        } ?: run {
                            binding.emptyContainerWatch.isVisible = true
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.uiStateRemoved.collect {
                it?.let {
                    adapter?.removeItemAt(it)
                    if (adapter?.itemCount == 0) {
                        when (currentCollectionType) {
                            CollectionType.FAVORITE -> binding.emptyContainer.isVisible = true
                            CollectionType.WATCHLIST -> binding.emptyContainerWatch.isVisible = true
                        }
                    } else {
                        when (currentCollectionType) {
                            CollectionType.FAVORITE -> binding.emptyContainer.isVisible = false
                            CollectionType.WATCHLIST -> binding.emptyContainerWatch.isVisible =
                                false
                        }
                    }
                }
            }
        }
    }

    private fun showMovies(movies: List<MovieDetails>) {
        adapter?.swapData(ArrayList(movies))
    }

    private fun itemClicked(movieId: String, title: String?) {
        val intent = Intent(activity, MovieDetailsActivity::class.java)
        intent.putExtra(MoviesFragment.SAVED_DATABASE_APPLICABLE, true)
        intent.putExtra(MoviesFragment.NETWORK_APPLICABLE, true)
        intent.putExtra(MoviesFragment.MOVIE_TITLE, title)
        intent.putExtra(MoviesFragment.MOVIE_ID, movieId)
        startActivity(intent)
    }

    private fun itemLongClicked(movie: MovieDetails, position: Int) {
        val adapter =
            ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1).apply {
                add(getString(R.string.remove))
            }
        MaterialAlertDialogBuilder(requireContext()).run {
            setAdapter(adapter) { _: DialogInterface?, _: Int ->
                when (currentCollectionType) {
                    CollectionType.FAVORITE -> movie.favorite = false
                    CollectionType.WATCHLIST -> movie.watchlist = false
                }
                viewModel.updateMovieDetailsInDb(movie, position)
            }
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
