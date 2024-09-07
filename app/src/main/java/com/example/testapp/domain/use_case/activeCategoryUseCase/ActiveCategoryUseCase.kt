package com.example.testapp.domain.use_case.activeCategoryUseCase

import com.example.testapp.data.remote.dto.searchCategory.CategoryResponseDto
import com.example.testapp.data.remote.dto.searchCategory.toCategoryModel
import com.example.testapp.domain.model.categoryModel.CategoryModel
import com.example.testapp.domain.repository.searchRepository.SearchRepository
import javax.inject.Inject

class ActiveCategoryUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend fun invoke(categoryId: Int,lang:String):List<CategoryModel>{
        return repository.getActiveSubCategories(categoryId = categoryId, lang = lang)

    }
}