package com.example.filmyapp.ui_layer.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2

class FilmyIntroActivity : AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(IntroFragmentA())
        addSlide(IntroFragmentB())
        addSlide(IntroFragmentC())
        addSlide(IntroFragmentD())

        showStatusBar(true)

        /*isProgressButtonEnabled = true
        setDepthAnimation()*/
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }
}