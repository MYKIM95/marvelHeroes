package com.mykim.marvelheroes.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mykim.common_util.visible
import com.mykim.common_util.windowWidth
import com.mykim.commonbase.BaseFragment
import com.mykim.marvelheroes.MainViewModel
import com.mykim.marvelheroes.adapter.FavoriteAdapter
import com.mykim.marvelheroes.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment @Inject constructor() : BaseFragment<FragmentFavoriteBinding>() {

    private val viewModel by viewModels<FavoriteViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()
    private var favoriteAdapter: FavoriteAdapter? = null

    override fun createFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFavoriteBinding.inflate(inflater, container, false)

    override fun initFragment() {
        initAdapter()
        collectViewModel()
    }

    private fun initAdapter() = with(binding) {
        favoriteAdapter = FavoriteAdapter(requireContext(), requestManager) { heroId ->
            viewModel.removeFavoriteHero(heroId)
        }
        val gridSpan = requireActivity().windowWidth() / 150.dp
        favoriteList.apply {
            layoutManager = GridLayoutManager(requireContext(), gridSpan)
            adapter = favoriteAdapter
        }
    }

    private fun collectViewModel() {

        mainViewModel.favoriteList.onResult { list ->
            if (list.isNotEmpty()) favoriteAdapter?.submit(list)
            binding.favoriteList.visible(list.isNotEmpty())
            binding.txtEmpty.visible(list.isEmpty())
        }

    }

}