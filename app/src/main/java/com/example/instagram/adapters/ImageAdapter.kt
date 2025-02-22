package com.example.instagram.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.instagram.R
import com.example.instagram.data.enums.Gender
import com.example.instagram.data.models.AuthorData
import com.example.instagram.data.models.PostData
import com.example.instagram.databinding.LayoutItemImageBinding

class ImageAdapter(
    private val images: List<String>,
    private val item: PostData,
    private val listener: OnClickImageListener,
    private val ivLike: AppCompatImageView,
    private val tvLikeCount: AppCompatTextView
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var lastClickTime: Long = 0

    inner class ImageViewHolder(val binding: LayoutItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            LayoutItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val zoomInAnim = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.zoom_in)
        val zoomOutAnim = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.zoom_out)
        val sharedPreferences = holder.itemView.context.getSharedPreferences("instagram", 0)
        val username = sharedPreferences.getString("username", "")
        val name = sharedPreferences.getString("name", "")
        val avatar = sharedPreferences.getString("avatar", "")

        if (images.isEmpty()) {
            holder.binding.ivImage.load(R.drawable.no_image)
        } else {
            holder.binding.ivImage.load(images[position]) {
                error(R.drawable.no_image)
            }
        }

        holder.binding.ivImage.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime < 300) {
                val isLiked = item.listLike.any { it.username == username.toString() }
                if (isLiked) {
                    item.totalLike -= 1
                    item.listLike.removeIf { it.username == username.toString() }
                    ivLike.setImageResource(R.drawable.ic_unlike)
                } else {
                    item.totalLike += 1
                    holder.binding.ivHeart.startAnimation(zoomOutAnim)
                    holder.binding.ivHeart.startAnimation(zoomInAnim)
                    ivLike.setImageResource(R.drawable.ic_liked)
                    item.listLike.add(AuthorData(username.toString(), name.toString(), avatar.toString(), Gender.MALE, "", ""))
                }
                tvLikeCount.text = item.totalLike.toString()
                listener.onDoubleClickImage(item)
            }

            lastClickTime = currentTime
        }
    }

    override fun getItemCount(): Int {
        return if (images.isEmpty()) 1 else images.size
    }

    interface OnClickImageListener {
        fun onDoubleClickImage(item: PostData)
    }
}

