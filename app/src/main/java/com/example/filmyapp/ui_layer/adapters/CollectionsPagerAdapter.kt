package com.example.filmyapp.ui_layer.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.filmyapp.ui_layer.collections.CollectionTypeFragment

class CollectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = CollectionTypeFragment.newInstance(
        if (position == 0) CollectionTypeFragment.CollectionType.FAVORITE else
            CollectionTypeFragment.CollectionType.WATCHLIST
    )
}