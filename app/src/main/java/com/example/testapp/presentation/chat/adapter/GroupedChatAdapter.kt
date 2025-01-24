package com.example.testapp.presentation.chat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.databinding.ItemDateSeparatorBinding
import com.example.testapp.domain.model.chat.ChatSortedByDateModel

class GroupedChatAdapter(
    private val layoutInflater: LayoutInflater
) : RecyclerView.Adapter<GroupedChatAdapter.GroupedChatViewHolder>() {

    private val groupedChats: MutableList<ChatSortedByDateModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupedChatViewHolder {
        return GroupedChatViewHolder(
            ItemDateSeparatorBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroupedChatViewHolder, position: Int) {
        holder.bind(groupedChats[position])
    }

    override fun getItemCount(): Int = groupedChats.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newGroupedChats: List<ChatSortedByDateModel>) {
        // Use DiffUtil to calculate changes
        val diffCallback = GroupedChatDiffCallback(groupedChats, newGroupedChats)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        // Update the current list and notify changes
        groupedChats.clear()
        groupedChats.addAll(newGroupedChats)
        diffResult.dispatchUpdatesTo(this)
    }

    class GroupedChatViewHolder(private val binding: ItemDateSeparatorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(groupedChat: ChatSortedByDateModel) {
            // Set the date
            binding.dateSeparatorText.text = groupedChat.date
            // Set up the nested RecyclerView for messages
            val messageAdapter = MessangerAdapter(LayoutInflater.from(binding.root.context))
            binding.messageRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
            binding.messageRecyclerView.adapter = messageAdapter

            // Populate messages for this date
            messageAdapter.setItems(groupedChat.messages)
            binding.messageRecyclerView.post {
                binding.messageRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
            }
        }
    }

    class GroupedChatDiffCallback(
        private val oldList: List<ChatSortedByDateModel>,
        private val newList: List<ChatSortedByDateModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].date == newList[newItemPosition].date
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
