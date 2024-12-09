package com.example.testapp.presentation.createAnnouncement.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.databinding.ItemCategoryBinding
import com.example.testapp.domain.model.categoryModel.CategoryModel

class SubCategoryAdapter(
    private val layoutInflater: LayoutInflater,
    private val onItemClick: (CategoryModel) -> Unit,
    private val showCount: Boolean
): RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>() {

    private val subCategoryList : MutableList<CategoryModel> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubCategoryAdapter.SubCategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(layoutInflater,parent,false)
        return SubCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubCategoryAdapter.SubCategoryViewHolder, position: Int) {
        holder.bind(subCategoryList[position])
    }

    override fun getItemCount(): Int {
      return subCategoryList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setSubCategoryItems(subCategoryList: List<CategoryModel>){
        this.subCategoryList.clear()
        this.subCategoryList.addAll(subCategoryList)
        notifyDataSetChanged()
    }
    inner class SubCategoryViewHolder(
        private val binding: ItemCategoryBinding,
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(categoryItem: CategoryModel){
            binding.subCategoryName.text = categoryItem.title
            if (categoryItem.hasChild){
                binding.iconMore.visibility = View.VISIBLE
            }else{
                binding.iconMore.visibility = View.GONE
            }
            /*if (showCount) {
                binding.count.text = categoryItem.itemsCount.toString()
                binding.count.visibility = View.VISIBLE
            } else {
                binding.count.visibility = View.GONE
            }*/
            binding.root.setOnClickListener {
                onItemClick(categoryItem)
            }

        }
    }
}