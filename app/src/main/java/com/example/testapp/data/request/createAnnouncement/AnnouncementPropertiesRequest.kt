package com.example.testapp.data.request.createAnnouncement

import kotlinx.serialization.SerialName

data class AnnouncementPropertiesRequest(
    @SerialName("lang")
    val lang: String,
    @SerialName("category_id")
    val categoryId: Int? = null,
    @SerialName("item_id")
    val editableAnnouncementId: Int? = null,
)
