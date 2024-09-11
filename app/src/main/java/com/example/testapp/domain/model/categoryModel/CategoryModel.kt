package com.example.testapp.domain.model.categoryModel

data class CategoryModel(
    val id: Int = -1,
    val title: String = "",
    val categoryId: Int = 0,
    val itemsCount: Int = 0,
    val icon: String = "",
    val keyword: String = "",
    val hasChild: Boolean = false,
    val storeCategoryId: Int? = null
)
