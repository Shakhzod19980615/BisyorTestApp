package com.example.testapp.domain.repository.createAnnouncement

import com.example.testapp.data.request.createAnnouncement.AnnouncementPropertiesRequest
import com.example.testapp.domain.model.createAnnouncement.AnnouncementDynamicPropertyModel

interface CreateAnnouncementRepository {
    suspend fun getAnnouncementDynamicProperties(param: AnnouncementPropertiesRequest)
    :List<AnnouncementDynamicPropertyModel>
}