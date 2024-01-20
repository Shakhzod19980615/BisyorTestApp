package com.example.testapp.data.repository.announcementDetails

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.announcementItemDetails.AnnouncementItemDetailsDto
import com.example.testapp.domain.repository.announcementItemDetails.AnnouncementDetailsRepository
import javax.inject.Inject

class AnnouncementDetailsRepositoryImpl @Inject constructor(
    private val api : AppService
):AnnouncementDetailsRepository {

    override suspend fun getAnnouncementDetails(itemId: Int?): AnnouncementItemDetailsDto {
        return api.getAnnouncementDetails(itemId = itemId?:-1)
    }
}