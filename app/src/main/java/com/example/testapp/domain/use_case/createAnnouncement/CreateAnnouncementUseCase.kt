package com.example.testapp.domain.use_case.createAnnouncement

import com.example.testapp.data.request.createAnnouncement.AnnouncementPropertiesRequest
import com.example.testapp.domain.model.createAnnouncement.AnnouncementDynamicPropertyModel
import com.example.testapp.domain.repository.createAnnouncement.CreateAnnouncementRepository
import javax.inject.Inject

class CreateAnnouncementUseCase @Inject constructor(
    private val createAnnouncementRepository: CreateAnnouncementRepository
) {
    suspend fun getAnnouncementDynamicProperties(param: AnnouncementPropertiesRequest):
            List<AnnouncementDynamicPropertyModel> {
        return createAnnouncementRepository.getAnnouncementDynamicProperties(param = param)
    }
}