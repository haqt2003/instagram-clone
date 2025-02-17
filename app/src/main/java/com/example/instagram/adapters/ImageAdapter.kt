package com.example.instagram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.instagram.R
import com.example.instagram.data.models.PostData
import com.example.instagram.databinding.LayoutItemImageBinding

class ImageAdapter(private val images: List<String>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

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
    }

    override fun getItemCount(): Int {
        return if (images.isEmpty()) 1 else images.size
    }
}

