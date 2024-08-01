package com.example.testapp.data.repositoryImpl.announcement

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.announcementList.AnnouncementListDto
import com.example.testapp.domain.repository.announcement.AnnouncementItemRepository
import javax.inject.Inject

class AnnouncementItemRepositoryImpl @Inject constructor(
    private val api : AppService
):AnnouncementItemRepository{
    override suspend fun getAnnouncementList(categoryId: Int): AnnouncementListDto {
        return api.getAnnouncementList(categoryId = categoryId)
    }
}