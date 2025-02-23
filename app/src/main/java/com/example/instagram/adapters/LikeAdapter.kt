package com.example.instagram.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.example.instagram.R
import com.example.instagram.data.models.AuthorData
import com.example.instagram.databinding.LayoutItemLikeBinding

class LikeAdapter(private val listener: OnClickListener) : BaseRecyclerView<LayoutItemLikeBinding, AuthorData>() {
    override fun getItemLayout(inflater: LayoutInflater, parent: ViewGroup): LayoutItemLikeBinding {
        return LayoutItemLikeBinding.inflate(inflater, parent, false)
    }

    override fun areContentsTheSame(oldItem: AuthorData, newItem: AuthorData): Boolean {
        return oldItem.username == newItem.username
    }

    override fun areItemsTheSame(oldItem: AuthorData, newItem: AuthorData): Boolean {
       return oldItem == newItem
    }

    override fun setData(binding: LayoutItemLikeBinding, item: AuthorData, layoutPosition: Int) {
        with(binding) {
            ivAvatar.load(item.avatar) {
                error(R.drawable.no_avatar)
            }
            tvUsername.text = item.username
            tvName.text = item.name

            ivAvatar.setOnClickListener {
                listener.onItemClicked(item)
            }

            clUser.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }

    interface OnClickListener {
        fun onItemClicked(item: AuthorData)
    }
}