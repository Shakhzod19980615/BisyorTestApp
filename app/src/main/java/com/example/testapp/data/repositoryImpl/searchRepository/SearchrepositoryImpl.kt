package com.example.testapp.data.repositoryImpl.searchRepository

import android.util.Log
import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.announcementList.toAnnouncementItem
import com.example.testapp.data.remote.dto.categoryTab.toCategoryTabItem
import com.example.testapp.data.remote.dto.searchCategory.CategoryResponseDto
import com.example.testapp.data.remote.dto.searchCategory.toCategoryModel
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
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

    override suspend fun getItemsByCategory(
        categoryId: Int,
        offset: Int,
        lang: String
    ): List<AnnouncementItemModel> {
        return try {
            api.getItemsByCategory(
                categoryId = categoryId,
                offset = offset,
                lang = lang
            ).items.map { it.toAnnouncementItem() }
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching items: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getItemsByQuery(
        query: String,
        offset: Int,
        lang: String,
        sorting: String?
    ): List<AnnouncementItemModel> {
        return try {
            api.getItemsByQuery(
                query = query,
                offset = offset,
                lang = lang,
                sorting = sorting
            ).items.map { it.toAnnouncementItem() }
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching items: ${e.message}", e)
            emptyList()
        }
    }

}