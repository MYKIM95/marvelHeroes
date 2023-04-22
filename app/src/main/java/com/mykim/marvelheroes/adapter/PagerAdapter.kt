package com.mykim.marvelheroes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mykim.marvelheroes.search.SearchFragment
import com.mykim.marvelheroes.favorite.FavoriteFragment

class PagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragmentCnt : Int
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = fragmentCnt

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SearchFragment()
            1 -> FavoriteFragment()
            else -> SearchFragment()
        }
    }

}