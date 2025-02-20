package com.example.testapp.presentation.favourite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.databinding.ItemProductGridBinding
import com.example.testapp.databinding.ItemSubscriptionBinding
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.model.userDataModel.SubscribedUserModel
import com.example.testapp.presentation.home.adapter.AnnouncementListAdapter

class SubscriptionsAdapter(
    private val layoutInflater: LayoutInflater,
):RecyclerView.Adapter<SubscriptionsAdapter.ViewHolder>() {
    private val subscriptionList : MutableList<SubscribedUserModel> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscriptionsAdapter.ViewHolder {
        val binding = ItemSubscriptionBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionsAdapter.ViewHolder, position: Int) {
        holder.bind(subscriptionList[position])
    }

    override fun getItemCount(): Int {
        return subscriptionList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setSubscriptionsItems(subscriptionList: List<SubscribedUserModel>){
        this.subscriptionList.clear()
        this.subscriptionList.addAll(subscriptionList)
        notifyDataSetChanged()
    }
    inner class ViewHolder(
        private val binding: ItemSubscriptionBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(subscriptionItem: SubscribedUserModel){
            Glide.with(binding.root).load(subscriptionItem.userAvatar).into(binding.itemImg)
            binding.userName.text = subscriptionItem.userName
            binding.userPhoneNumber.text = subscriptionItem.userPhone

            val itemAdapter = AnnouncementListAdapter(
                LayoutInflater.from(binding.root.context),
                onItemClicked = {},
                onFavouriteClicked = {},
            )
            binding.list.layoutManager = LinearLayoutManager(binding.root.context,
                LinearLayoutManager.HORIZONTAL, false)
            binding.list.adapter = itemAdapter
            itemAdapter.setAnnouncementItems(subscriptionItem.items)
        }
    }
}