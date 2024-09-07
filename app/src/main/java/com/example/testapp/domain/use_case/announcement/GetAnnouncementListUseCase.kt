package com.example.testapp.domain.use_case.announcement

import com.example.testapp.data.remote.dto.announcementList.toAnnouncementItem
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.repository.announcement.AnnouncementItemRepository
import com.example.testapp.domain.repository.categoryTab.CategoryTabRepository
import javax.inject.Inject

class GetAnnouncementListUseCase @Inject constructor(
    private val repository: AnnouncementItemRepository
) {
    suspend fun invoke(categoryId: Int): List<AnnouncementItemModel> {
        return try {
            repository.getAnnouncementList(categoryId)
        }catch (e: Exception){
            emptyList()
        }

    }
}