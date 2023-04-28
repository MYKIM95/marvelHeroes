package com.mykim.marvelheroes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.RequestManager
import com.mykim.commonbase.BaseDiffUtilAdapter
import com.mykim.commonbase.BaseViewHolder
import com.mykim.core_model.HeroData
import com.mykim.marvelheroes.R
import com.mykim.marvelheroes.databinding.ItemHeroListBinding

class SearchAdapter(
    private val context: Context,
    override val requestManager: RequestManager,
    private val onClick: (HeroData) -> Unit
) : BaseDiffUtilAdapter<ItemHeroListBinding, HeroData>(requestManager) {

    override fun getBinding(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemHeroListBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    inner class ViewHolder(
        itemHeroListBinding: ItemHeroListBinding
    ) : BaseViewHolder<ItemHeroListBinding>(itemHeroListBinding) {
        override fun bind(position: Int) = with(binding) {
            val data = adapterList[position]

            imgThumb.setImage(data.thumbnail.toImageUrl())
            txtName.text = data.name
            txtDesc.text = data.description
            container.background = if (data.isFavorite) AppCompatResources.getDrawable(
                context,
                R.drawable.bg_orange_storke_grey_r_16
            )
            else AppCompatResources.getDrawable(context, R.drawable.bg_white_stroke_grey_r_16)

            container.setOnClickListener {
                onClick.invoke(data)
            }
        }
    }

    override fun areItemsTheSame(oldItem: HeroData, newItem: HeroData): Boolean =
        oldItem.heroId == newItem.heroId

    override fun areContentsTheSame(oldItem: HeroData, newItem: HeroData): Boolean =
        oldItem.isFavorite == newItem.isFavorite

}