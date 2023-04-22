package com.mykim.marvelheroes.search

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mykim.commonbase.BaseFragment
import com.mykim.marvelheroes.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment @Inject constructor() : BaseFragment<FragmentSearchBinding>() {


    override fun createFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBinding.inflate(inflater, container, false)

    override fun initFragment() {

    }

}