package com.mykim.marvelheroes

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.mykim.common_util.onUiState
import com.mykim.common_util.showToast
import com.mykim.commonbase.BaseActivity
import com.mykim.marvelheroes.adapter.PagerAdapter
import com.mykim.marvelheroes.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()
    private val fragmentCount = 2

    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initActivity() {
        collectViewModel()
        initAdapter()
        initTabLayout()
        getData()
    }

    private fun collectViewModel() = with(viewModel) {
        favoriteListState.onUiState(
            scope = lifecycleScope,
            success = {
                viewModel.setFavoriteList(it)
            },
            error = {
                this@MainActivity.showToast(R.string.favorite_list_error)
            }
        )
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

    private fun getData() {
        viewModel.getFavoriteList()
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