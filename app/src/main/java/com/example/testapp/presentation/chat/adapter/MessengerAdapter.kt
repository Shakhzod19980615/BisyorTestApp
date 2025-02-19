package com.example.testapp.presentation.chat.adapter

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.common.util.MyUtil
import com.example.testapp.databinding.ItemMessageBinding
import com.example.testapp.databinding.ItemMessageDocumentBinding
import com.example.testapp.databinding.ItemMessageImageBinding
import com.example.testapp.domain.model.chat.Message

class MessengerAdapter(
    private val layoutInflater: LayoutInflater,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messagesList: MutableList<Message> = mutableListOf()

    //private val groupedMessages: MutableList<GroupedMessage> = mutableListOf()
    companion object {
        const val TYPE_TEXT = "text"
        const val TYPE_FILE = "file"
        const val TYPE_IMAGE = "image"

    }

    override fun getItemViewType(position: Int): Int {
        return when (messagesList[position].type) {
            TYPE_TEXT -> 0
            TYPE_FILE -> 1
            TYPE_IMAGE -> 2
            else -> throw IllegalArgumentException("Invalid message type")
        }
    }

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messagesList[position]
        when (holder) {
            is TextMessageViewHolder -> holder.bind(message)
            is FileMessageViewHolder -> holder.bind(message)
            is ImageMessageViewHolder -> holder.bind(message)
        }
    }


    override fun getItemCount(): Int = messagesList.size
    @SuppressLint("NotifyDataSetChanged")
    fun setItems(messagesList: List<Message>) {
        this.messagesList.clear()
        this.messagesList.addAll(messagesList)
        notifyDataSetChanged()
    }


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