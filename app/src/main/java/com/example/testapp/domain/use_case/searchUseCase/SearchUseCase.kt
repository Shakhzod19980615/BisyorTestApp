package com.example.testapp.domain.use_case.searchUseCase

import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.model.categoryModel.CategoryModel
import com.example.testapp.domain.repository.searchRepository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend fun getActiveCategoryList(categoryId: Int, lang:String):List<CategoryModel>{
        return repository.getActiveSubCategories(categoryId = categoryId, lang = lang)

    }
    suspend fun getItemsByCategory(
        categoryId: Int,
        offset: Int,
        lang: String
    ):List<AnnouncementItemModel>{
        return repository.getItemsByCategory(
            categoryId = categoryId,
            offset = offset,
            lang = lang
        )
    }

    suspend fun getItemsByQuery(
        query:String,
        offset:Int,
        lang:String,
        sorting:String?
    ):List<AnnouncementItemModel>{
        return repository.getItemsByQuery(
            query = query,
            offset = offset,
            lang = lang,
            sorting = sorting
        )
    }
}