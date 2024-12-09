package com.example.testapp.domain.model.categoryModel

data class CategoryModel(
    val id: Int = -1,
    val title: String = "",
    val icon: String = "",
    val categoryId: Int = 0,
    val itemsCount: Int = 0,
    val hasChild: Boolean = false,
    val storeCategoryId: Int? = null
)
