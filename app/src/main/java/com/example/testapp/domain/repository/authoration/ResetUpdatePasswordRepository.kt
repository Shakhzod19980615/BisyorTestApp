package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.request.resetPassword.ResetUserUpdatePasswordRequest

interface ResetUpdatePasswordRepository {
    suspend fun updatePassword(resetUserUpdatePasswordRequest: ResetUserUpdatePasswordRequest): UserDataResponse
}