package com.example.testapp.presentation.chat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.common.util.MyUtil
import com.example.testapp.databinding.ItemMessageBinding
import com.example.testapp.databinding.ItemMessageDocumentBinding
import com.example.testapp.databinding.ItemMessageImageBinding
import com.example.testapp.domain.model.chat.GroupedMessage
import com.example.testapp.domain.model.chat.Message

class MessangerAdapter(
    private val layoutInflater: LayoutInflater,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messagesList: MutableList<Message> = mutableListOf()

    //private val groupedMessages: MutableList<GroupedMessage> = mutableListOf()
    companion object {
        const val TYPE_TEXT = "text"
        const val TYPE_FILE = "file"
        const val TYPE_IMAGE = "image"

    }

    /*companion object {
        private const val TYPE_TEXT = 0
        private const val TYPE_FILE = 1
        private const val TYPE_IMAGE = 2
        private const val TYPE_DATE = 3
    }*/
    override fun getItemViewType(position: Int): Int {
        return when (messagesList[position].type) {
            TYPE_TEXT -> 0
            TYPE_FILE -> 1
            TYPE_IMAGE -> 2
            else -> throw IllegalArgumentException("Invalid message type")
        }
    }
    /* override fun getItemViewType(position: Int): Int {
        return when (groupedMessages[position]) {
            is GroupedMessage.DateSeparator -> TYPE_DATE
            is GroupedMessage.ChatMessage -> {
                when ((groupedMessages[position] as GroupedMessage.ChatMessage).message.type) {
                    "text" -> TYPE_TEXT
                    "file" -> TYPE_FILE
                    "image" -> TYPE_IMAGE
                    else -> throw IllegalArgumentException("Invalid message type")
                }
            }

        }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> TextMessageViewHolder(
                ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            1 -> FileMessageViewHolder(
                ItemMessageDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            2 -> ImageMessageViewHolder(
                ItemMessageImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TEXT -> TextMessageViewHolder(
                ItemMessageBinding.inflate(layoutInflater, parent, false)
            )
            TYPE_FILE -> FileMessageViewHolder(
                ItemMessageDocumentBinding.inflate(layoutInflater, parent, false)
            )
            TYPE_IMAGE -> ImageMessageViewHolder(
                ItemMessageImageBinding.inflate(layoutInflater, parent, false)
            )
            TYPE_DATE -> DateSeparatorViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_date_separator, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }*/

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messagesList[position]
        when (holder) {
            is TextMessageViewHolder -> holder.bind(message)
            is FileMessageViewHolder -> holder.bind(message)
            is ImageMessageViewHolder -> holder.bind(message)
        }
    }
    /*override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = groupedMessages[position]) {
            is GroupedMessage.DateSeparator -> {
                if (holder is DateSeparatorViewHolder) holder.bind(item.date)
            }
            is GroupedMessage.ChatMessage -> {
                when (holder) {
                    is TextMessageViewHolder -> holder.bind(item.message)
                    is FileMessageViewHolder -> holder.bind(item.message)
                    is ImageMessageViewHolder -> holder.bind(item.message)
                }
            }

        }
    }*/

    override fun getItemCount(): Int = messagesList.size
    @SuppressLint("NotifyDataSetChanged")
    fun setItems(messagesList: List<Message>) {
        this.messagesList.clear()
        this.messagesList.addAll(messagesList)
        notifyDataSetChanged()
    }

    /*fun setItems(messages: List<Message>) {
        groupedMessages.clear()

        // Group messages by date and ensure the date comes first
        val groupedByDate = messages.groupBy { message ->
            MyUtil.formatDateLocalized(layoutInflater.context, message.date_cr, "HH:mm dd.MM.yyyy")
        }

        for ((date, messagesOnDate) in groupedByDate.entries) {
            // Add the date first
            groupedMessages.add(GroupedMessage.DateSeparator(date))
            // Add all messages for this date
            messagesOnDate.forEach { message ->
                groupedMessages.add(GroupedMessage.ChatMessage(message))
            }
        }

        notifyDataSetChanged()
    }*/
   /* fun setItems(messages: List<Message>) {
        groupedMessages.clear()

        // Step 1: Sort messages by their timestamp (ascending order)
        val sortedMessages = messages.sortedBy { it.date_cr }

        // Step 2: Group messages by date
        val groupedByDate = sortedMessages.groupBy { message ->
            // Format date as needed (e.g., "HH:mm dd.MM.yyyy")
            MyUtil.formatDateLocalized(layoutInflater.context, message.date_cr, "HH:mm dd.MM.yyyy")
        }

        // Step 3: Add the date and messages to groupedMessages in the correct order
        for ((date, messagesOnDate) in groupedByDate) {
            // Add the date first
            groupedMessages.add(GroupedMessage.DateSeparator(date))
            // Add all messages for this date
            messagesOnDate.forEach { message ->
                groupedMessages.add(GroupedMessage.ChatMessage(message))
            }
        }

        // Notify the adapter that the data has changed
        notifyDataSetChanged()
    }*/


   /* class DateSeparatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateText: TextView = itemView.findViewById(R.id.date_separator_text)
        fun bind(date: String) {
            dateText.text = date
        }
    }*/
    class TextMessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "ResourceAsColor")
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
                    it.marginEnd = 4 // Add your desired margin
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

    // ViewHolder for file messages
    class FileMessageViewHolder(private val binding: ItemMessageDocumentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.messegeText.text = message.message.ifEmpty { "No file name" }
            binding.messegeTime.text = message.date_cr

            if (message.creator == "me") {
                binding.messegeText.gravity = View.TEXT_ALIGNMENT_TEXT_END
            } else {
                binding.messegeText.gravity = View.TEXT_ALIGNMENT_TEXT_START
            }
        }
    }

    // ViewHolder for image messages
    class ImageMessageViewHolder(private val binding: ItemMessageImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            // Assuming `message.message` contains the image URL
            if (message.message.isNotEmpty()) {
                Glide.with(binding.root).load(message.message)
                    .into(binding.image)// Load the image URL into the ImageView
            } else {
                binding.image.setImageResource(R.drawable.image_loading_placeholder) // Show a placeholder
            }
            binding.messegeTime.text = message.date_cr
            val layoutParams = binding.image.layoutParams as LinearLayout.LayoutParams
            if (message.creator == "me") {
                layoutParams.gravity = Gravity.END // Align to the right
            } else {
                layoutParams.gravity = Gravity.START // Align to the left
            }
            binding.image.layoutParams = layoutParams
        }
    }


}