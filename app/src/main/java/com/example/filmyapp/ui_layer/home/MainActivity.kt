package com.example.filmyapp.ui_layer.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.filmyapp.R
import com.example.filmyapp.databinding.ActivityMainBinding
import com.example.filmyapp.ui_layer.intro.FilmyIntroActivity
import com.example.filmyapp.ui_layer.search.SearchViewModel
import com.example.filmyapp.utility.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModelSearch: SearchViewModel by viewModels()
    private var throughShortcut: Boolean? = null

    // private var cantProceed = false
    private var darkMode = false
    private lateinit var binding: ActivityMainBinding

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val statusCode = intent.getIntExtra("message", 0)
            cantProceed(statusCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        throughShortcut = intent.getBooleanExtra("throughShortcut", false)

        if (darkMode) darkThemeLogic() else lightThemeLogic()
        introLogic()
        setupNavigation()
        observerUiStates()
    }

    private fun setupTheme() {
        darkMode = PreferenceHelper.isDarkModeEnabled(this)
        if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)
            == Configuration.UI_MODE_NIGHT_YES
        ) {
            darkMode = true
            PreferenceHelper.setDarkModeEnabled(this, true)
        }

        if (darkMode) setTheme(R.style.AppTheme_MD3_Dark) else setTheme(R.style.AppTheme_MD3)
    }

    private fun lightThemeLogic() {
        binding.navigationBarView.backgroundTintList = null
    }

    private fun darkThemeLogic() {
        binding.navigationBarView.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.colorDarkThemePrimaryDark)
    }

    private fun observerUiStates() {
        lifecycleScope.launch {
            viewModelSearch.uiStateSearchView.collect {
                it?.let {
                    when (it) {
                        SearchViewUiState.Hidden -> {
                            animateBottomNavigation(0)
                        }

                        SearchViewUiState.Visible -> {
                            animateBottomNavigation(-250)
                        }
                    }
                }
            }
        }
    }

    private fun animateBottomNavigation(newBottomMargin: Int) {
        binding.navigationBarView.clearAnimation()
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val params = binding.navigationBarView.layoutParams as ConstraintLayout.LayoutParams
                params.bottomMargin = (newBottomMargin * interpolatedTime).toInt()
                binding.navigationBarView.layoutParams = params
            }
        }
        a.duration = 100
        binding.navigationBarView.startAnimation(a)
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.navigationBarView, navController)

        if (throughShortcut == true) {
            navController.navigate(R.id.collections)
            binding.navigationBarView.selectedItemId = R.id.collections
        }
    }

    private fun introLogic() {
        if (PreferenceHelper.isColdStart(this)) {
            val intent = Intent(this@MainActivity, FilmyIntroActivity::class.java)
            startActivity(intent)
            PreferenceHelper.setColdStartDone(this)
        }
    }

    fun cantProceed(status: Int) {
        /* Handler().postDelayed({
             if (moviesFragment != null && !moviesFragment!!.isShowingFromDatabase) {
                 cantProceed = true
                 //binding.viewpager.visibility = View.GONE
                 //binding.mainErrorView.visibility = View.VISIBLE
                 disableToolbarScrolling()
             }
         }, 1000)*/
    }

    private fun canProceed() {
        // cantProceed = false
        //binding.viewpager.visibility = View.VISIBLE
        //binding.mainErrorView.visibility = View.GONE
        //trendingFragment?.retryLoading()
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            if (viewModelSearch.isSearchOpen.value) {
                viewModelSearch.closeSearch()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (darkMode != PreferenceHelper.isDarkModeEnabled(this)) recreate()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mMessageReceiver, IntentFilter("fetch-failed"))
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onPause()
    }
}