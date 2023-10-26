package com.example.filmyapp.ui_layer.full

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.filmyapp.R
import com.example.filmyapp.data_layer.local.models.TrailerData
import com.example.filmyapp.databinding.AllTrailerLayoutBinding
import com.example.filmyapp.ui_layer.adapters.MovieTrailersAdapter
import com.example.filmyapp.ui_layer.youtube_webview.YoutubeWebviewActivity
import com.example.filmyapp.utility.PreferenceHelper
import themeSystemBars
import kotlin.math.hypot

class AllTrailersFragment : Fragment() {

    private var movieTitle: String? = null
    private var trailers: Array<TrailerData>? = null
    private var darkMode = false
    private var _binding: AllTrailerLayoutBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val MOVIE_TITLE = "MOVIE_TITLE"
        const val TRAILERS = "TRAILERS"

        fun newInstance(title: String?, trailers: Array<TrailerData>): AllTrailersFragment {
            val args = Bundle()
            args.putString(MOVIE_TITLE, title)
            args.putParcelableArray(TRAILERS, trailers)
            val fragment = AllTrailersFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        darkMode = PreferenceHelper.isDarkModeEnabled(requireContext())
        _binding = AllTrailerLayoutBinding.inflate(inflater, container, false)
        if (!darkMode) allThemeLogic() else nightModeLogic()

        binding.cross.setOnClickListener {
            binding.cross.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            activity?.supportFragmentManager?.popBackStack()
        }

        binding.root.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int,
                oldRight: Int, oldBottom: Int
            ) {
                v.removeOnLayoutChangeListener(this)
                val cx = arguments?.getInt("cx") ?: 0
                val cy = arguments?.getInt("cy") ?: 0
                val radius = hypot(right.toDouble(), bottom.toDouble()).toInt()
                ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, radius.toFloat()).run {
                    interpolator = DecelerateInterpolator(2f)
                    duration = 1000
                    start()
                }
            }
        })

        requireActivity().themeSystemBars(!darkMode, lightStatusBar = true)
        return binding.root
    }

    private fun nightModeLogic() {
        binding.mainContent.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.surfaceColorDark
            )
        )
        binding.textViewTitle.setTextColor(Color.parseColor("#ffffff"))
    }

    private fun allThemeLogic() {
        binding.mainContent.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.surfaceColorLight
            )
        )
        binding.textViewTitle.setTextColor(Color.parseColor("#000000"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieTitle = arguments?.getString(MOVIE_TITLE, " ")
        trailers = arguments?.getParcelableArray(TRAILERS) as Array<TrailerData>
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewTitle.text = movieTitle

        binding.allTrailerRecyclerView.adapter = trailers?.let {
            MovieTrailersAdapter(it) { trailerData ->
                trailerData.url?.let { id ->
                    playTrailerOnYoutube(id)
                }
            }
        }
        binding.allTrailerRecyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun playTrailerOnYoutube(trailerId: String) {

        val intent: Intent = Intent(requireContext(), YoutubeWebviewActivity::class.java)
        intent?.let {
            it.putExtra(YoutubeWebviewActivity.VEDIO_ID, trailerId)
            startActivity(it)
        }

        /*startActivity(
            YouTubeStandalonePlayer.createVideoIntent(
                activity,
                "YOUTUBE_API_KEY",
                trailerId,
                0,
                true,
                false
            )
        )*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().themeSystemBars(!darkMode, lightStatusBar = false)
        _binding = null
    }
}