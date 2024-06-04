package com.example.testapp.domain.model.userDataModel

data class UserDataModel(
    val token: String,
    val userId: Int,
    val userName: String,
    val userAvatar: String,
    val genderId: Int,
    val userPhoneNumber: String,
    val userLogin: String,
    val userEmail:String,
    val userStatusId: Int,
    val userBalance: Int,
    val balanceFormatted: String,
    val bonusBalance: Int,
    val userBirthday: String,
    val itemCount: Int,
    val storeItemCount: Int,
    val districtId: Int,
    val districtName: String,
    val regionId: Int,
    val regionName: String,
    val messageCount: Int
)