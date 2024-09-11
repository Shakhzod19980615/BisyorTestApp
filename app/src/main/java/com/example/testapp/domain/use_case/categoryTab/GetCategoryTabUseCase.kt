package com.example.testapp.domain.use_case.categoryTab

import com.example.testapp.data.remote.dto.categoryTab.toCategoryTabItem
import com.example.testapp.domain.model.categoryModel.CategoryModel
import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel
import com.example.testapp.domain.repository.categoryTab.CategoryTabRepository
import javax.inject.Inject

class GetCategoryTabUseCase @Inject constructor(
    private val repository: CategoryTabRepository
) {
     suspend fun invoke():List<CategoryModel>{
        return repository.getAllMainCategories()
     }
}