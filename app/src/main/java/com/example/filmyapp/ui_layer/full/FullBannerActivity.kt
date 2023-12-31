package com.example.filmyapp.ui_layer.full

import android.graphics.Bitmap
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.filmyapp.databinding.ActivityFullScreenImageBinding

class FullBannerActivity : AppCompatActivity() {

    private var bannerImageUrl: String? = null
    private lateinit var binding: ActivityFullScreenImageBinding

    companion object {
        const val IMAGE_URL = "IMAGE_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.breathingProgress.visibility = View.VISIBLE
        bannerImageUrl = intent?.getStringExtra(IMAGE_URL)

        binding.cross.setOnClickListener {
            binding.cross.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            onBackPressed()
        }

        Glide.with(this)
            .asBitmap()
            .load(bannerImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    binding.ivBanner.setImageBitmap(resource)
                    binding.breathingProgress.visibility = View.INVISIBLE
                }
            })
    }
}