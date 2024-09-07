package com.example.testapp.domain.repository.announcement

import com.example.testapp.data.remote.dto.announcementList.AnnouncementListDto
import com.example.testapp.domain.model.announcement.AnnouncementItemModel

interface AnnouncementItemRepository {
    suspend fun getAnnouncementList(categoryId: Int): List<AnnouncementItemModel>
}