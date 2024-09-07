package com.example.testapp.data.repositoryImpl.announcement

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.announcementList.toAnnouncementItem
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.repository.announcement.AnnouncementItemRepository
import javax.inject.Inject

class AnnouncementItemRepositoryImpl @Inject constructor(
    private val api : AppService
):AnnouncementItemRepository{
    override suspend fun getAnnouncementList(categoryId: Int): List<AnnouncementItemModel> {
        return try {
            api.getAnnouncementList(categoryId = categoryId).items.map { it.toAnnouncementItem() }
        }catch (e:Exception){
            emptyList()
        }

    }
}