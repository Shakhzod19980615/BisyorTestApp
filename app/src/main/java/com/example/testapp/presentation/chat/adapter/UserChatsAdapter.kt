package com.example.testapp.presentation.chat.adapter

import ChatModel
import UserChat
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.data.remote.dto.announcementItemDetails.User
import com.example.testapp.databinding.ItemChatBinding
import com.example.testapp.databinding.ItemProductGridBinding
import com.example.testapp.presentation.home.adapter.AnnouncementListAdapter

class UserChatsAdapter(
    private val layoutInflater: LayoutInflater,
    private val onItemClicked: (chatId:Int) -> Unit
): RecyclerView.Adapter<UserChatsAdapter.ViewHolder> ()  {
    private  val chatList :MutableList<UserChat> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChatsAdapter.ViewHolder {
        val binding = ItemChatBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserChatsAdapter.ViewHolder, position: Int) {
       holder.bind(chatList[position])
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setItems(chatList: List<UserChat>) {
        this.chatList.clear()
        this.chatList.addAll(chatList)
        notifyDataSetChanged()
    }
    inner class ViewHolder(
        private val binding: ItemChatBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(chat: UserChat){
            if(chat.item?.title.isNullOrEmpty()){
                binding.userName.text = chat.user.fullName


            }
            else{
                binding.userName.text =  chat.item?.title
            }
            if (chat.item?.imageUrl.isNullOrEmpty()) {
                binding.itemImg.visibility = View.GONE
                val params = binding.date.layoutParams as LinearLayout.LayoutParams
                params.gravity = LinearLayout.TEXT_ALIGNMENT_VIEW_END
                binding.date.layoutParams = params
            } else {
                binding.itemImg.visibility = View.VISIBLE
                Glide.with(binding.root).load(chat.item?.imageUrl).into(binding.itemImg)
                val params = binding.date.layoutParams as LinearLayout.LayoutParams
                params.gravity = LinearLayout.TEXT_ALIGNMENT_VIEW_START
                binding.date.layoutParams = params
            }
            binding.userName.text = chat.user.fullName
            binding.message.text = chat.lastMessage.text
            binding.date.text = chat.lastMessage.createdAt
            Glide.with(binding.root).load(chat.user.avatarUrl).into(binding.userAvatar)

            itemView.setOnClickListener {
                val item = chatList[adapterPosition]
                onItemClicked(item.chatId)
            }
        }
    }
}