package com.example.testapp.presentation.home.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.databinding.ItemProductGridBinding
import com.example.testapp.domain.model.announcement.AnnouncementItemModel

class AnnouncementListAdapter (
    private val layoutInflater: LayoutInflater,
    private val onItemClicked: (itemId:Int) -> Unit,
    private val onFavouriteClicked: (itemId: Int) -> Unit,
): RecyclerView.Adapter<AnnouncementListAdapter.ViewHolder> () {
    private var announcementList : MutableList<AnnouncementItemModel> = mutableListOf()
    private var favouriteList: MutableList<Int> = mutableListOf()
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
    fun setAnnouncementItems(newList: List<AnnouncementItemModel>){
        val diffCallback = ItemsDiffCallBack(announcementList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        announcementList.clear()
        announcementList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)

    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateFavouriteList(newFavourites: List<Int>) {
        val diffCallback = FavouriteDiffCallback(favouriteList, newFavourites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        favouriteList.clear()
        favouriteList.addAll(newFavourites)
        notifyDataSetChanged()
        //favouriteList = newFavourites.toMutableList()
        diffResult.dispatchUpdatesTo(this)
       // Log.d("AnnouncementListAdapter", "Favourite list updated: $favouritesList")
    }
    @SuppressLint("NotifyDataSetChanged")
    fun removeItemById(itemId: Int) {
        val index = announcementList.indexOfFirst { it.id == itemId }
        if (index != -1) {
            announcementList.removeAt(index)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(
        private val binding: ItemProductGridBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(announcementItem: AnnouncementItemModel){
            binding.title.text = announcementItem.title
            binding.price.text = announcementItem.price
            Glide.with(binding.root).load(announcementItem.img_m).into(binding.img)
            // Check if the item ID is in the favouriteList
            val isFavorite = favouriteList.contains(announcementItem.id)
            binding.starButton.setImageResource(
                if (isFavorite) R.drawable.vicon_favorite_active
                else R.drawable.vicon_favorite_inactive
            )
            binding.baseLay.setOnClickListener {
                val item = announcementList[adapterPosition]
                onItemClicked(item.id)
            }

            binding.starButton.setOnClickListener {
                //val newFavoriteStatus = !isFavorite
                onFavouriteClicked(announcementItem.id)
            }


        }

        init {
            itemView.setOnClickListener {
                val item = announcementList[adapterPosition]
                onItemClicked(item.id)
            }
        }
    }

}
class ItemsDiffCallBack(
    private val oldList: List<AnnouncementItemModel>,
    private val newList: List<AnnouncementItemModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].favorites == newList[newItemPosition].favorites
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
class FavouriteDiffCallback(
    private val oldList: List<Int>,
    private val newList: List<Int>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

