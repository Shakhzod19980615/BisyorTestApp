package com.example.testapp.domain.use_case.categoryTab

import com.example.testapp.data.remote.dto.categoryTab.toCategoryTabItem
import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel
import com.example.testapp.domain.repository.categoryTab.CategoryTabRepository
import javax.inject.Inject

class GetCategoryTabUseCase @Inject constructor(
    private val repository: CategoryTabRepository
) {
     suspend fun invoke():List<CategoryTabItemModel>{
        return repository.getAllMainCategories().map { it.toCategoryTabItem() }
    }
}