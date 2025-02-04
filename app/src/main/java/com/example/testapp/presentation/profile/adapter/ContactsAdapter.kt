package com.example.testapp.presentation.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.databinding.ItemContactBinding
import com.example.testapp.databinding.ItemProductGridBinding
import com.example.testapp.domain.model.ContactModel
import com.example.testapp.presentation.home.adapter.AnnouncementListAdapter

class ContactsAdapter(
    // Use the Contact data class
    private val layoutInflater: LayoutInflater,
    private val onContactSelected: (ContactModel) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    private val contacts: MutableList<ContactModel> = mutableListOf()
    private var selectedContact: MutableList<ContactModel> = mutableListOf()
    // ViewHolder for the Contact item

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactsAdapter.ViewHolder {
        val binding = ItemContactBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }


    // Bind data to the views
    override fun onBindViewHolder(holder: ContactsAdapter.ViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    // Return the number of contacts
    override fun getItemCount(): Int {
        return contacts.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(contacts: List<ContactModel>) {
        this.contacts.clear()
        this.contacts.addAll(contacts)
        notifyDataSetChanged()
    }
    inner class ViewHolder(
        private val binding: ItemContactBinding
    ): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("ResourceAsColor")
        fun bind(contact: ContactModel){
            binding.tvName.text = contact.name
            binding.tvNumber.text = contact.phoneNumber
            Glide.with(binding.root).load(contact.name).into(binding.ivIcon)
            val initials = getInitials(contact.name)
            binding.clRoot.setOnClickListener {
                if (selectedContact.contains(contact)){
                    selectedContact.remove(contact)
                }else{
                    selectedContact.add(contact)
                }
                onContactSelected(contact)
                notifyItemChanged(adapterPosition)
            }
            // Set the initials to the TextView (tvAvatar)
            binding.tvAvatar.text = initials
            if (selectedContact.contains(contact)){
                binding.tvAvatar.visibility = View.GONE
                binding.ivIcon.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.vicon_radio_checked)
                binding.clRoot.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.someColor
                    )
                )
            }else{
                binding.tvAvatar.visibility = View.VISIBLE
                binding.ivIcon.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.bg_circle_1)
                binding.tvAvatar.text = initials
                binding.clRoot.setBackgroundColor(Color.TRANSPARENT)
            }


            // If you want to load an image (e.g., from a URL), you can use Glide
            // Otherwise, you can ignore this part

        }
    }
    private fun getInitials(name: String): String {
        if (name.isBlank()) return "?" // Fallback for empty names

        val words = name.split(" ").filter { it.isNotEmpty() } // Split and filter out empty words
        val initials = StringBuilder()

        // Append the first letter of the first two words
        for (i in 0 until minOf(2, words.size)) {
            initials.append(words[i][0]) // Append the first character of each word
        }

        return initials.toString().uppercase() // Convert to uppercase for consistency
    }
}