package com.example.testapp.domain.model.categoryModel

data class CategoryModel(
    val id: Int = -1,
    val title: String = "",
    val categoryId: Int = 0,
    val itemCount: Int = 0,
    val hasChild : Boolean = false,
    val storeCategoryId: Int? = null
)
