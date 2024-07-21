package com.example.testapp.presentation.createAnnouncement.adapter

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.databinding.ItemAddProductImageBinding

class CreateAnnouncementAdapter(
)
    : ListAdapter<CreateAnnouncementImage, CreateAnnouncementAdapter.ImageViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemAddProductImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class ImageViewHolder(
        private val binding: ItemAddProductImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val handler = Handler(Looper.getMainLooper())
        fun bind(imageItem: CreateAnnouncementImage) {
            binding.img.setImageURI(imageItem.uri)
            binding.uploadingLayout.isVisible = imageItem.uploadState == UploadState.UPLOADING
            binding.reupdateImage.isVisible = imageItem.uploadState == UploadState.FAILED

            binding.img.setOnClickListener {
                showDeleteIcon()
            }
        }
        private fun showDeleteIcon() {
            binding.delete.isVisible = true
            handler.removeCallbacks(hideDeleteIconRunnable)
            handler.postDelayed(hideDeleteIconRunnable, 4000) // Adjust the delay as needed (5000ms = 5 seconds)
        }

        private val hideDeleteIconRunnable = Runnable {
            binding.delete.isVisible = false
        }
    }
    class DiffCallback : DiffUtil.ItemCallback<CreateAnnouncementImage>() {
        override fun areItemsTheSame(oldItem: CreateAnnouncementImage, newItem: CreateAnnouncementImage): Boolean {
            return oldItem.uri == newItem.uri
        }

        override fun areContentsTheSame(oldItem: CreateAnnouncementImage, newItem: CreateAnnouncementImage): Boolean {
            return oldItem == newItem
        }
    }
}