package com.example.testapp.common

import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel
import com.example.testapp.domain.model.userDataModel.UserDataModel

sealed class ApiResponse {
    data class Success(val userData: UserDataModel) : ApiResponse()
    data class Error(val errorResponse: BasicResponseModel) : ApiResponse()
}