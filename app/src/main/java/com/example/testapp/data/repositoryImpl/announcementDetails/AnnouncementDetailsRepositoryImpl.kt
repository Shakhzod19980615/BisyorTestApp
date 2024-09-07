package com.example.testapp.data.repositoryImpl.announcementDetails

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.announcementItemDetails.AnnouncementItemDetailsDto
import com.example.testapp.data.remote.dto.announcementItemDetails.toAnnouncmentItemDetails
import com.example.testapp.domain.model.announcementItemDetails.AnnouncementItemDetailsModel
import com.example.testapp.domain.repository.announcementItemDetails.AnnouncementDetailsRepository
import javax.inject.Inject

class AnnouncementDetailsRepositoryImpl @Inject constructor(
    private val api : AppService
):AnnouncementDetailsRepository {

    override suspend fun getAnnouncementDetails(itemId: Int?): AnnouncementItemDetailsModel {
        return try {
            api.getAnnouncementDetails(itemId = itemId?:-1).toAnnouncmentItemDetails()
        }catch (e:Exception){
            AnnouncementItemDetailsModel()
        }


    }
}