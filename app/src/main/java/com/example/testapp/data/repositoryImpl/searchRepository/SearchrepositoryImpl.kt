package com.example.testapp.data.repositoryImpl.searchRepository

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.searchCategory.CategoryResponseDto
import com.example.testapp.data.remote.dto.searchCategory.toCategoryModel
import com.example.testapp.domain.model.categoryModel.CategoryModel
import com.example.testapp.domain.repository.searchRepository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
   private val api:AppService
): SearchRepository {
    override suspend fun getActiveSubCategories(categoryId: Int,lang:String): List<CategoryModel> {
        return try {
            api.getActiveInsideCategories(categoryId = categoryId, lang = lang)
                .map { it.toCategoryModel() }
        }catch (e:Exception){
            emptyList()
        }
    }
}