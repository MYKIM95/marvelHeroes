package com.mykim.marvelheroes.adapter

import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.mykim.commonbase.BaseDiffUtilAdapter
import com.mykim.commonbase.BaseViewHolder
import com.mykim.core_model.HeroData
import com.mykim.marvelheroes.databinding.ItemHeroListBinding

class SearchAdapter(
    override val requestManager: RequestManager
) : BaseDiffUtilAdapter<ItemHeroListBinding, HeroData>(requestManager) {

    override fun getBinding(parent: ViewGroup, viewType: Int): BaseViewHolder<ItemHeroListBinding> {
        TODO("Not yet implemented")
    }

    override fun areContentsTheSame(oldItem: HeroData, newItem: HeroData): Boolean {
        TODO("Not yet implemented")
    }

    override fun areItemsTheSame(oldItem: HeroData, newItem: HeroData): Boolean {
        TODO("Not yet implemented")
    }


}