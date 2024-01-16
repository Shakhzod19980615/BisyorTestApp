package com.example.testapp.domain.use_case.announcementDetails

import com.example.testapp.data.remote.dto.announcementItemDetails.toAnnouncmentItemDetails
import com.example.testapp.domain.model.announcementItemDetails.AnnouncementItemDetailsModel
import com.example.testapp.domain.repository.announcementItemDetails.AnnouncementDetailsRepository
import javax.inject.Inject

class GetAnnouncementDetailsUseCase @Inject constructor(
    private val repository: AnnouncementDetailsRepository
) {
    suspend fun invoke(itemId: Int): AnnouncementItemDetailsModel {
        return repository.getAnnouncementDetails(itemId = itemId).toAnnouncmentItemDetails()

    }
}