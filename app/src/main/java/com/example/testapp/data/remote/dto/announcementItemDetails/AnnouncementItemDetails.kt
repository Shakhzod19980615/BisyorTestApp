package com.example.testapp.data.remote.dto.announcementItemDetails

import com.google.gson.annotations.SerializedName

data class AnnouncementItemDetails(
    @SerializedName("dynamic-fields")
    val dynamic_fields: ArrayList<DynamicField>,
    @SerializedName("main-fields")
    val main_fields: MainFields,
    val user: User
)