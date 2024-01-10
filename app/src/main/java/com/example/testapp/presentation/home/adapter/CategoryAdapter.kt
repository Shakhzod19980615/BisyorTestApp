package com.example.testapp.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.databinding.ItemTabBinding
import com.example.testapp.domain.model.CategoryItem

class CategoryAdapter(
    private val layoutInflater: LayoutInflater,
): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private val categoryList : MutableList<CategoryItem> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryViewHolder {
        val binding = ItemTabBinding.inflate(layoutInflater,parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
      return categoryList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryItems(categoryList: List<CategoryItem>){
        this.categoryList.clear()
        this.categoryList.addAll(categoryList)
        notifyDataSetChanged()
    }

inner class CategoryViewHolder(
    private val binding: ItemTabBinding
): RecyclerView.ViewHolder(binding.root){
    fun bind(categoryItem: CategoryItem){
        binding.name.text = categoryItem.title
        Glide.with(binding.root).load(categoryItem.icon).into(binding.image)
    }
}
}