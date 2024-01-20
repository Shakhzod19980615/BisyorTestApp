package com.example.testapp.domain.repository.announcementItemDetails

import com.example.testapp.data.remote.dto.announcementItemDetails.AnnouncementItemDetailsDto

interface AnnouncementDetailsRepository {

    suspend fun getAnnouncementDetails(itemId: Int?): AnnouncementItemDetailsDto
}