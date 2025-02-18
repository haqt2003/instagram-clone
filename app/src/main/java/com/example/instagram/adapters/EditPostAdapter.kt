package com.example.instagram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.example.instagram.R
import com.example.instagram.databinding.LayoutItemEditBinding

class EditPostAdapter(private val listener: OnClickListener) : BaseRecyclerView<LayoutItemEditBinding, String>() {
    override fun getItemLayout(inflater: LayoutInflater, parent: ViewGroup): LayoutItemEditBinding {
        return LayoutItemEditBinding.inflate(inflater, parent, false)
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
       return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun setData(binding: LayoutItemEditBinding, item: String, layoutPosition: Int) {
       with(binding) {
           ivContent.load(item) {
               error(R.drawable.no_image)
           }
           ivDelete.setOnClickListener {
               listener.onItemClicked(item)
           }
       }
    }

    interface OnClickListener {
        fun onItemClicked(item: String)
    }
}