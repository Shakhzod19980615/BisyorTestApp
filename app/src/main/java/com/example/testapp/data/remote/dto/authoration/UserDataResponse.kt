package com.example.testapp.data.remote.dto.authoration

import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("access_token")
    val token: String,
    val userId:Int,
    val userFIO:String,
    val avatar:String,
    val genderId:Int,
    val userPhone:String,
    val login:String,
    val email:String,
    val statusId:Int,
    val balance:Int,
    val balanceString:String,
    val bonusBalance:Int,
    val birthday:String,
    val itemCount:Int,
    val getShopsItemCount:Int,
    val districtId:Int,
    val districtName:String,
    val regionId:Int,
    val regionName:String,
    val messageCount:Int
)
fun UserDataResponse.toUserDataModel() = UserDataModel(
    token = token,
    userId = userId,
    userName = userFIO,
    userAvatar = avatar,
    genderId = genderId,
    userPhoneNumber = userPhone,
    userLogin = login,
    userEmail = email,
    userStatusId = statusId,
    userBalance = balance,
    balanceFormatted = balanceString,
    bonusBalance = bonusBalance,
    userBirthday = birthday,
    itemCount = itemCount,
    storeItemCount = getShopsItemCount,
    districtId = districtId,
    districtName = districtName,
    regionId = regionId,
    regionName = regionName,
    messageCount = messageCount,

)