package com.example.testapp.domain.repository.announcement

import com.example.testapp.data.remote.dto.announcementList.AnnouncementListDto

interface AnnouncementItemRepository {
    suspend fun getAnnouncementList(categoryId: Int): AnnouncementListDto
}