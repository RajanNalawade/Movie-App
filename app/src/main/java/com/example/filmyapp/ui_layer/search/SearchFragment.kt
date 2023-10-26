package com.example.filmyapp.ui_layer.search

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmyapp.data_layer.local.models.SearchResult
import com.example.filmyapp.databinding.FragmentSearchBinding
import com.example.filmyapp.ui_layer.adapters.SearchResultAdapter
import com.example.filmyapp.ui_layer.details.MovieDetailsActivity
import com.example.filmyapp.ui_layer.home.MoviesFragment.Companion.FROM_ACTIVITY
import com.example.filmyapp.ui_layer.home.MoviesFragment.Companion.MOVIE_ID
import com.example.filmyapp.ui_layer.home.MoviesFragment.Companion.MOVIE_TITLE
import com.example.filmyapp.ui_layer.home.MoviesFragment.Companion.NETWORK_APPLICABLE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "SEARCH_FRAGMENT"
        fun newInstance(): SearchFragment {
            val args = Bundle()
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]

        val spanCount = when (activity?.resources?.configuration?.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 3
            else -> 5
        }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
        observerUiState()
        return view
    }

    private fun observerUiState() {
        lifecycleScope.launch {
            viewModel.uiStateSearchResults.collect {
                it?.let {
                    showSearchResults(it)
                }
            }
        }
    }

    private fun itemClicked(searchData: SearchResult, position: Int) {
        Intent(activity, MovieDetailsActivity::class.java).run {
            putExtra(NETWORK_APPLICABLE, true)
            putExtra(MOVIE_TITLE, searchData.originalTitle)
            putExtra(MOVIE_ID, searchData.id.toString())
            putExtra(FROM_ACTIVITY, false)
            startActivity(this)
        }
    }

    private fun showSearchResults(results: List<SearchResult>) {
        val adapter = SearchResultAdapter(results) { searchData, position ->
            itemClicked(searchData, position)
        }
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerView.adapter = adapter

        hideProgress()
        hideSoftKeyboard()
    }

    fun showProgress() {
        binding.breathingProgress.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
    }

    private fun hideProgress() {
        binding.breathingProgress.visibility = View.INVISIBLE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun hideSoftKeyboard() {
        if (activity != null && activity?.currentFocus != null) {
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}