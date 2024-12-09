package com.example.testapp.presentation.createAnnouncement.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapp.databinding.ItemMainCategoryBinding
import com.example.testapp.databinding.ItemProductGridBinding
import com.example.testapp.databinding.WindowCategoryPickerBinding
import com.example.testapp.domain.model.categoryModel.CategoryModel
import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel
import com.example.testapp.presentation.home.adapter.AnnouncementListAdapter
import kotlinx.coroutines.flow.MutableStateFlow

class CategoryPickerAdapter(

    private val layoutInflater: LayoutInflater,
    private val onItemClick: (Int) -> Unit
): RecyclerView.Adapter<CategoryPickerAdapter.ViewHolder>() {
    private val category:MutableList<CategoryModel> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryPickerAdapter.ViewHolder {
        val binding = ItemMainCategoryBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryPickerAdapter.ViewHolder, position: Int) {
        holder.bind(category[position])
    }

    override fun getItemCount(): Int {
        return category.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryItems(categoryList: List<CategoryModel>){
        this.category.clear()
        this.category.addAll(categoryList)
        notifyDataSetChanged()
    }
    inner class ViewHolder(
        private val binding: ItemMainCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(categoryItem: CategoryModel) {
            binding.categoryName.text = categoryItem.title
            binding.count.text = categoryItem.itemsCount.toString()
            Glide.with(binding.root).load(categoryItem.icon).into(binding.imgHome)
            binding.root.setOnClickListener {
                onItemClick(categoryItem.categoryId)
            }
        }
    }

}