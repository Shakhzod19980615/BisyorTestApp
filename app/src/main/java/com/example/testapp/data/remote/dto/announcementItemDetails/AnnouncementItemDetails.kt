package com.example.testapp.data.remote.dto.announcementItemDetails

data class AnnouncementItemDetails(
    val dynamic_fields: ArrayList<DynamicField>,
    val main_fields: MainFields,
    val user: User
)