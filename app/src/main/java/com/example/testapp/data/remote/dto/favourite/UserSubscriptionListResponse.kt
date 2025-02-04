package com.example.testapp.data.remote.dto.favourite

import com.example.testapp.data.remote.dto.announcementList.AnnouncementItemDto
import com.example.testapp.data.remote.dto.announcementList.toAnnouncementItem
import com.example.testapp.data.remote.dto.createAnnouncement.AnnouncementDynamicResponse
import com.example.testapp.domain.model.userDataModel.SubscribedUserModel

data class UserSubscriptionListResponse(
    val hasNextPage: Boolean,
    val itemCount: Int?,
    val totalCount: Int?,
    val users:List<SubscribedUserResponse>
)

data class SubscribedUserResponse(
    val id:Int,
    val user:SubscribedUserInfo,
    val items:List<AnnouncementItemDto>?,
)

data class SubscribedUserInfo(
    val userId:Int,
    val userFIO:String?,
    val phone:String?,
    val email:String?,
    val avatar:String?,
)

fun SubscribedUserResponse.toSubscribedUserModel() = SubscribedUserModel(
    id = id,
    userId = user.userId,
    userName = user.userFIO?:"",
    userAvatar = user.avatar?:"",
    userPhone = user.phone?:"",
    items = items?.map { it.toAnnouncementItem() }?:emptyList()
)