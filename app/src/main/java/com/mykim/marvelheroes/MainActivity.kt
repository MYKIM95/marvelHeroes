package com.mykim.marvelheroes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.mykim.commonbase.BaseActivity
import com.mykim.marvelheroes.adapter.PagerAdapter
import com.mykim.marvelheroes.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val fragmentCount = 2

    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initActivity() {

        initAdapter()
        initTabLayout()

    }

    private fun initAdapter() = with(binding) {

        viewPager.adapter = PagerAdapter(this@MainActivity, fragmentCount)

    }

    private fun initTabLayout() = with(binding) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = "Search"
                1 -> tab.text = "Storage"
            }
        }.attach()
    }

    override fun onActivityBackPressed() {
        super.onActivityBackPressed()

        if(binding.viewPager.currentItem == 0) {
            // TODO 닫는 다이얼로그
        }else {
            binding.viewPager.currentItem -= 1
        }

    }

}