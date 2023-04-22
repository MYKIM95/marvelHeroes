package com.mykim.marvelheroes.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mykim.commonbase.BaseFragment
import com.mykim.marvelheroes.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment @Inject constructor() : BaseFragment<FragmentFavoriteBinding>() {

    override fun createFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFavoriteBinding.inflate(inflater, container, false)

    override fun initFragment() {
        initAdapter()
    }

    private fun initAdapter() {

    }

}