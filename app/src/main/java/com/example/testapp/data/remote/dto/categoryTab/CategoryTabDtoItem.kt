package com.example.testapp.data.remote.dto.categoryTab

import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel

data class CategoryDtoItem(
    val addr: Int,
    val categoryId: Int,
    val icon: String,
    val keyword: String,
    val title: String
)
fun CategoryDtoItem.toCategoryTabItem() = CategoryTabItemModel(
    addr = addr,
    categoryId = categoryId,
    icon = icon,
    keyword = keyword,
    title = title
)