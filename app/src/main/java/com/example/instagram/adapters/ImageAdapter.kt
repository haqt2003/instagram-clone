package com.example.instagram.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.instagram.R
import com.example.instagram.data.models.PostData
import com.example.instagram.databinding.LayoutItemImageBinding

class ImageAdapter(
    private val images: List<String>,
    private val item: PostData,
    private val listener: OnClickImageListener
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

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
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

