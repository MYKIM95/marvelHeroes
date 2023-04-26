package com.mykim.marvelheroes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.mykim.commonbase.BaseDiffUtilAdapter
import com.mykim.commonbase.BaseViewHolder
import com.mykim.core_database.entity.FavoriteTable
import com.mykim.marvelheroes.databinding.ItemFavoriteListBinding

class FavoriteAdapter(
    private val context: Context,
    override val requestManager: RequestManager,
    private val onClick: (Int) -> Unit
) : BaseDiffUtilAdapter<ItemFavoriteListBinding, FavoriteTable>(requestManager) {

    override fun getBinding(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemFavoriteListBinding> = ViewHolder(
        ItemFavoriteListBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    inner class ViewHolder(
        itemFavoriteListBinding: ItemFavoriteListBinding
    ) : BaseViewHolder<ItemFavoriteListBinding>(itemFavoriteListBinding) {

        override fun bind(position: Int) = with(binding) {
            val data = adapterList[position]

            imgThumb.setImage(data.thumbnail)
            txtName.text = data.name
            txtDesc.text = data.desc

            container.setOnClickListener {
                onClick.invoke(data.heroId)
            }
        }
    }

    override fun areItemsTheSame(oldItem: FavoriteTable, newItem: FavoriteTable): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: FavoriteTable, newItem: FavoriteTable): Boolean =
        oldItem.heroId == newItem.heroId

}