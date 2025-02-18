package com.example.instagram.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.example.instagram.R
import com.example.instagram.data.models.PostData
import com.example.instagram.databinding.LayoutItemLinearBinding
import com.example.instagram.utils.formatDate

class HomeAdapter(private val listener: OnClickListener) : BaseRecyclerView<LayoutItemLinearBinding, PostData>(), ImageAdapter.OnClickImageListener{
    override fun getItemLayout(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): LayoutItemLinearBinding {
       return LayoutItemLinearBinding.inflate(inflater, parent, false)
    }

    override fun areContentsTheSame(oldItem: PostData, newItem: PostData): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areItemsTheSame(oldItem: PostData, newItem: PostData): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun setData(binding: LayoutItemLinearBinding, item: PostData, layoutPosition: Int) {
        val sharedPreferences = binding.root.context.getSharedPreferences("instagram", 0)
        val username = sharedPreferences.getString("username", "")

        with(binding) {
            if (item.author.avatar == "") {
                ivAvatar.setImageResource(R.drawable.no_avatar)
            } else {
                ivAvatar.load(item.author.avatar) {
                    placeholder(R.drawable.no_avatar)
                    error(R.drawable.no_avatar)
                }
            }

            vpContent.isNestedScrollingEnabled = false
            vpContent.adapter = ImageAdapter(item.images, item, this@HomeAdapter)
            ciIndi.setViewPager(vpContent)

            if (item.listLike.any { it.username == username.toString() }) {
                ivLike.setImageResource(R.drawable.ic_liked)
            } else {
                ivLike.setImageResource(R.drawable.ic_unlike)
            }
            tvName.text = item.author.username
            tvLikeCount.text = item.totalLike.toString()
            tvNameTitle.text = item.author.username
            tvTitle.text = item.content ?: ""
            tvDate.text =  formatDate(item.updatedAt)
            ivLike.setOnClickListener {
                listener.onLikeClicked(item)
            }
            ivMore.setOnClickListener {
                listener.onMoreClicked(item)
            }
        }
    }

    interface OnClickListener {
        fun onLikeClicked(item: PostData)
        fun onItemDoubleClicked(item: PostData)
        fun onMoreClicked(item: PostData)
    }

    override fun onDoubleClickImage(item: PostData) {
        listener.onItemDoubleClicked(item)
    }
}