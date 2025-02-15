package com.example.instagram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.example.instagram.R
import com.example.instagram.data.models.PostData
import com.example.instagram.databinding.LayoutItemGridBinding

class ProfileAdapter(private val listener: OnClickListener) : BaseRecyclerView<LayoutItemGridBinding, PostData>() {
    override fun getItemLayout(inflater: LayoutInflater, parent: ViewGroup): LayoutItemGridBinding {
        return LayoutItemGridBinding.inflate(inflater, parent, false)
    }

    override fun areContentsTheSame(oldItem: PostData, newItem: PostData): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areItemsTheSame(oldItem: PostData, newItem: PostData): Boolean {
        return oldItem == newItem
    }

    override fun setData(binding: LayoutItemGridBinding, item: PostData, layoutPosition: Int) {
        with(binding) {
            if (item.images.isNotEmpty()) {
                ivSquare.load(item.images[0]) {
                    error(R.drawable.img)
                }
            } else {
                ivSquare.setImageResource(R.drawable.no_image)
            }

            ivSquare.setOnClickListener {
                listener.onClickItem(item)
            }
        }
    }

    interface OnClickListener {
        fun onClickItem(item: PostData)
    }
}