package com.example.instagram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import com.example.instagram.databinding.LayoutItemLinearHoriBinding

class AddPostAdapter : BaseRecyclerView<LayoutItemLinearHoriBinding, String>() {
    override fun getItemLayout(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): LayoutItemLinearHoriBinding {
        return LayoutItemLinearHoriBinding.inflate(inflater, parent, false)
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun setData(binding: LayoutItemLinearHoriBinding, item: String, layoutPosition: Int) {
        with(binding) {
            ivContent.setImageURI(item.toUri())
        }
    }
}