package com.example.testapp.domain.model.userDataModel

import com.example.testapp.domain.model.announcement.AnnouncementItemModel

data class SubscribedUserModel(
    val id: Int,
    val userId: Int,
    val userName: String,
    val userAvatar: String,
    val userPhone: String,
    val items: List<AnnouncementItemModel>
)
