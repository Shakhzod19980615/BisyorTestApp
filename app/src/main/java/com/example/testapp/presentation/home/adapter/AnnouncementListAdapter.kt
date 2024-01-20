package com.example.testapp.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.databinding.ItemProductGridBinding
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel

class AnnouncementListAdapter (
    private val layoutInflater: LayoutInflater,
    private val onItemClicked: (itemId:Int) -> Unit
): RecyclerView.Adapter<AnnouncementListAdapter.ViewHolder> () {
    private val announcementList : MutableList<AnnouncementItemModel> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementListAdapter.ViewHolder {
        val binding = ItemProductGridBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementListAdapter.ViewHolder, position: Int) {
        holder.bind(announcementList[position])
    }

    override fun getItemCount(): Int {
       return announcementList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAnnouncementItems(announcementList: List<AnnouncementItemModel>){
        this.announcementList.clear()
        this.announcementList.addAll(announcementList)
        notifyDataSetChanged()
    }
    inner class ViewHolder(
        private val binding: ItemProductGridBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(announcementItem: AnnouncementItemModel){
            binding.title.text = announcementItem.title
            binding.price.text = announcementItem.price
            Glide.with(binding.root).load(announcementItem.img_m).into(binding.img)
           /* binding.cardNewsCategory.setOnClickListener {
                val item = announcementList[adapterPosition]
                onItemClicked(item.id)
            }*/
           /* binding.starButton.setOnClickListener {
                val item = announcementList[adapterPosition]
                onItemClicked(item.id)
            }*/
            binding.baseLay.setOnClickListener {
                val item = announcementList[adapterPosition]
                onItemClicked(item.id)
            }
        }

        /*init {
            itemView.setOnClickListener {
                val item = announcementList[adapterPosition]
                onItemClicked(item.id)
            }
        }*/
    }

}