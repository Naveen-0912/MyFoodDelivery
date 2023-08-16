package com.example.myfooddelivery.ui.screens.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfooddelivery.constants.ViewPagerMenus

class ScreenSlidePagerAdapter(fragmentManager: FragmentManager,
    lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = ViewPagerMenus.values().size

    override fun createFragment(position: Int): Fragment {
        return if (position == ViewPagerMenus.HOTEL_TAB.ordinal)
            HotelFragment()
        else
            FoodItemsFragment()
    }
}