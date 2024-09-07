package com.example.testapp.domain.repository.announcementItemDetails

import com.example.testapp.data.remote.dto.announcementItemDetails.AnnouncementItemDetailsDto
import com.example.testapp.domain.model.announcementItemDetails.AnnouncementItemDetailsModel

interface AnnouncementDetailsRepository {

    suspend fun getAnnouncementDetails(itemId: Int?): AnnouncementItemDetailsModel
}