package com.example.testapp.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.common.util.MyUtil
import com.example.testapp.databinding.ItemMessageBinding
import com.example.testapp.domain.model.chat.ChatSortedByDateModel
import com.example.testapp.domain.model.chat.Message

class ChatAdapter(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val DATE_VIEW = 0
        private const val MESSAGE_VIEW = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) DATE_VIEW else MESSAGE_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATE_VIEW) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_date_separator, parent, false)
            DateViewHolder(view)
        } else {

            MessageViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DateViewHolder) {
            holder.bind(items[position] as String)
        } else if (holder is MessageViewHolder) {
            holder.bind(items[position] as Message)
        }
    }

    override fun getItemCount(): Int = items.size

    class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.date_separator_text)

        fun bind(date: String) {
            dateTextView.text = date
        }
    }

    class MessageViewHolder(private val binding: ItemMessageBinding)
        : RecyclerView.ViewHolder(binding.root) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messege_text)
        private val timeTextView: TextView = itemView.findViewById(R.id.messege_time)

        fun bind(message: Message) {
            // Handle null message text gracefully
            binding.messegeText.text = message.message.ifEmpty { "No content" }
            binding.messegeTime.text = MyUtil.formatTimeLocalized(itemView.context,message.date_cr, "HH:mm dd.MM.yyyy")

            // Adjust layout based on the creator (me/you)
            if (message.creator == "me") {
                binding.card.setCardBackgroundColor(
                    binding.root.context.getColor(R.color.messageColorBlue)
                )
                binding.messegeText.gravity = Gravity.END
                binding.messegeText.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )
                binding.messegeTime.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.gravity = Gravity.END
                binding.card.layoutParams = layoutParams
                val messageLayoutParams = binding.messageLL.layoutParams as? ViewGroup.MarginLayoutParams
                messageLayoutParams?.let {
                    it.marginStart = 100
                    it.marginEnd = 4
                    binding.messageLL.layoutParams = it
                }


            } else {
                binding.card.setCardBackgroundColor(
                    binding.root.context.getColor(R.color.messageColorGrey)
                )
                binding.messegeText.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.black)
                )
                binding.messegeTime.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.black)
                )
                val messageLayoutParams = binding.messageLL.layoutParams as? ViewGroup.MarginLayoutParams
                messageLayoutParams?.let {
                    it.marginStart = 4
                    it.marginEnd = 100 // Add your desired margin
                    binding.messageLL.layoutParams = it
                }
                binding.messegeText.gravity = Gravity.START
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.gravity = Gravity.START
                binding.card.layoutParams = layoutParams
            }
        }
    }
}
