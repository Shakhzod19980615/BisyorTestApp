package com.example.testapp.data.repositoryImpl.createAnnouncement

import android.util.Log
import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.createAnnouncement.toAnnouncementDynamicPropertyModel
import com.example.testapp.data.request.createAnnouncement.AnnouncementPropertiesRequest
import com.example.testapp.domain.model.createAnnouncement.AnnouncementDynamicPropertyModel
import com.example.testapp.domain.repository.createAnnouncement.CreateAnnouncementRepository
import javax.inject.Inject

class CreateAnnouncementRepositoryImpl @Inject constructor(
    private val api: AppService
): CreateAnnouncementRepository {
    override suspend fun getAnnouncementDynamicProperties(param: AnnouncementPropertiesRequest):
            List<AnnouncementDynamicPropertyModel> {
        return try {
            //api.getDynamicItemsByCategoryId(param).map { it.toAnnouncementDynamicPropertyModel() }
            val response = api.getDynamicItemsByCategoryId(param)
           // Log.d("ResponseDynamic", "Raw API Response: $response")
            val toModel = response.map { item ->
                item.toAnnouncementDynamicPropertyModel() }
            Log.d("ResponseDynamic", "Mapped items: $toModel")
           toModel

        }catch (e:Exception){
            emptyList()
        }
    }
}