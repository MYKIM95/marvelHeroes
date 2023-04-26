package com.mykim.marvelheroes.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mykim.common_util.showToast
import com.mykim.common_util.visible
import com.mykim.common_util.windowWidth
import com.mykim.commonbase.BaseFragment
import com.mykim.marvelheroes.MainViewModel
import com.mykim.marvelheroes.R
import com.mykim.marvelheroes.adapter.SearchAdapter
import com.mykim.marvelheroes.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment @Inject constructor() : BaseFragment<FragmentSearchBinding>() {

    private val viewModel by viewModels<SearchViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()
    private var searchAdapter: SearchAdapter? = null

    override fun createFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBinding.inflate(inflater, container, false)

    override fun initFragment() {
        initAdapter()
        collectViewModel()
        setTextWatcher()
    }

    private fun initAdapter() = with(binding) {
        searchAdapter = SearchAdapter(requireContext(), requestManager) { data ->
            if (data.isFavorite) viewModel.removeFavoriteHero(data)
            else viewModel.addFavoriteHero(data)
        }

        val gridSpan = requireActivity().windowWidth() / 150.dp
        searchList.apply {
            layoutManager = GridLayoutManager(requireContext(), gridSpan)
            adapter = searchAdapter
            addOnScrollListener(scrollListener)
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when {
                !binding.searchList.canScrollVertically(1)
                        && viewModel.heroList.value.size < viewModel.total -> {
                    viewModel.searchHero()
                }

                !binding.searchList.canScrollVertically(1) -> {
                    requireContext().showToast(R.string.search_end)
                }
            }
        }
    }

    private fun collectViewModel() = with(viewModel) {

        mainViewModel.favoriteList.onResult {
            viewModel.setFavoriteList(it)
        }

        searchQuery
            .debounce(300)
            .onEach {
                viewModel.isFirstSearch = true
                viewModel.offset = 0
                viewModel.heroName = it
                viewModel.searchHero()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        heroList.onResult { list ->
            if (list.isNotEmpty()) {
                viewModel.isFirstSearch = false
                searchAdapter?.submit(list)
            }
            binding.searchList.visible(list.isNotEmpty())
            binding.txtEmpty.visible(list.isEmpty())
        }

        heroDataState.onUiState(
            success = {
                viewModel.setHeroList(it.data.results)
            },
            error = {
                binding.loadingView.visible(false)
                requireContext().showToast(R.string.search_error)
            },
            loading = {
                binding.loadingView.visible()
            },
            finish = {
                binding.loadingView.visible(false)
            }
        )
    }

    private fun setTextWatcher() = with(binding) {
        editSearch.addTextChangedListener { str ->
            viewModel.setSearchQuery(str.toString())
        }
    }

}