package com.example.testapp.domain.model.userDataModel

data class UserDataModel(
    val token: String? = null,
    val userId: Int = 0,
    val userName: String? = null,
    val userAvatar: String?=null,
    val genderId: Int=0,
    val userPhoneNumber: String?=null,
    val userLogin: String?=null,
    val userEmail:String?=null,
    val userStatusId: Int?=null,
    val userBalance: Int?=null,
    val balanceFormatted: String?=null,
    val bonusBalance: Int?=null,
    val userBirthday: String?=null,
    val itemCount: Int?=null,
    val storeItemCount: Int,
    val districtId: Int?=null,
    val districtName: String?=null,
    val regionId: Int?=null,
    val regionName: String?=null,
    val messageCount: Int?=null
)