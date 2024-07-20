package com.example.testapp.presentation.createAnnouncement.adapter

import android.net.Uri
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

class CreateAnnouncementAdapter()
    : ListAdapter<CreateAnnouncementImage, CreateAnnouncementAdapter.ImageViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_product_image,
            parent, false)
        return ImageViewHolder(view)
    }
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.img)
        private val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        private val uploadingLayout: RelativeLayout = view.findViewById(R.id.uploading_layout)
        private val failedView: View = view.findViewById(R.id.reupdate_image)
        private val deleteView: ImageView = view.findViewById(R.id.delete)

        fun bind(imageItem: CreateAnnouncementImage) {
            imageView.setImageURI(imageItem.uri)
            uploadingLayout.isVisible = imageItem.uploadState == UploadState.UPLOADING
            failedView.isVisible = imageItem.uploadState == UploadState.FAILED
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