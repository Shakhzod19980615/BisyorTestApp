package com.example.testapp.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.R
import com.example.testapp.databinding.ItemTabBinding
import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel

class CategoryTabAdapter(
    private val layoutInflater: LayoutInflater,
    val onTabClicked: (Int) -> Unit
): RecyclerView.Adapter<CategoryTabAdapter.CategoryViewHolder>() {
    private val categoryList : MutableList<CategoryTabItemModel> = mutableListOf()
    private var selectedCategoryIndex = 0
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryTabAdapter.CategoryViewHolder {
        val binding = ItemTabBinding.inflate(layoutInflater,parent,false)
        return CategoryViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CategoryTabAdapter.CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
         if (selectedCategoryIndex == position) {
             holder.itemView.setBackgroundResource(R.drawable.tab_selected_bg)
        } else {
             holder.itemView.setBackgroundResource(R.drawable.tab_unselected_bg)
        }

    }

    override fun getItemCount(): Int {
      return categoryList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryItems(categoryList: List<CategoryTabItemModel>){
        this.categoryList.clear()
        this.categoryList.addAll(categoryList)
        notifyDataSetChanged()
    }

@SuppressLint("NotifyDataSetChanged")
inner class CategoryViewHolder(
    private val binding: ItemTabBinding,
): RecyclerView.ViewHolder(binding.root){
    @SuppressLint("ResourceAsColor")
    fun bind(categoryItem: CategoryTabItemModel){
        binding.name.text = categoryItem.title
        Glide.with(binding.root).load(categoryItem.icon).into(binding.image)
        updateTextColor()
    }
    private fun updateTextColor() {
        if (selectedCategoryIndex == adapterPosition) {
            binding.name.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        } else {
            binding.name.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
        }
    }
    init {
        itemView.setOnClickListener {
            val item = categoryList[adapterPosition]
            onTabClicked(item.categoryId)
            selectedCategoryIndex = adapterPosition
            notifyDataSetChanged()
        // Handle item click or any other actions here
        }
    }
}
}