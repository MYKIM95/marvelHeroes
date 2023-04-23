package com.mykim.marvelheroes.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mykim.common_util.*
import com.mykim.commonbase.BaseFragment
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
    private var searchAdapter: SearchAdapter? = null

    override fun createFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBinding.inflate(inflater, container, false)

    override fun initFragment() {
        initAdapter()
        collectViewModel()
        setClickListeners()
        setTextWatcher()
    }

    private fun initAdapter() = with(binding) {
        searchAdapter = SearchAdapter(requireContext(), requestManager) {
            // 좋아요 표시

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

        searchQuery
            .debounce(300)
            .onEach {
                Log.i("123123123", "each : $it")
                viewModel.isFirstSearch = true
                viewModel.offset = 0
                viewModel.heroName = it
                viewModel.searchHero()
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        heroList.onResult(viewLifecycleOwner.lifecycleScope) { list ->
            if(list.isNotEmpty()) {
                viewModel.isFirstSearch = false
                searchAdapter?.submit(list)
            }
            binding.searchList.visible(list.isNotEmpty())
            binding.txtEmpty.visible(list.isEmpty())
        }

        heroDataState.onUiState(
            scope = viewLifecycleOwner.lifecycleScope,
            success = {
                viewModel.setHeroList(it.data.results)
            },
            error = {
                Log.d("123123123", "error = $it")
            },
            loading = {
                Log.d("123123123", "loading...")
            },
            finish = {
                Log.d("123123123", "finish...")
            }
        )
    }

    private fun setClickListeners() = with(binding) {

    }

    private fun setTextWatcher() = with(binding) {
        editSearch.addTextChangedListener { str ->
            Log.i("123123123", "emit : ${str.toString()}")
            viewModel.setSearchQuery(str.toString())
        }
    }

}