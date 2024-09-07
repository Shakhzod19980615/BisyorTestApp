package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.request.resetPassword.ResetUserUpdatePasswordRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel

interface ResetUpdatePasswordRepository {
    suspend fun updatePassword(resetUserUpdatePasswordRequest: ResetUserUpdatePasswordRequest): UserDataModel
}