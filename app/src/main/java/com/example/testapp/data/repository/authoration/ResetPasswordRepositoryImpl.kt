package com.example.testapp.data.repository.authoration

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.remote.dto.basicResponse.BasicResponseDto
import com.example.testapp.data.request.resetPassword.ResetUserUpdatePasswordRequest
import com.example.testapp.data.request.resetUser.ResetUserRequest
import com.example.testapp.domain.repository.authoration.ResetUpdatePasswordRepository
import com.example.testapp.domain.repository.authoration.ResetUserRepository
import javax.inject.Inject

class ResetPasswordRepositoryImpl @Inject constructor(

    private val api : AppService
): ResetUpdatePasswordRepository {
    override suspend fun updatePassword(resetUserUpdatePasswordRequest: ResetUserUpdatePasswordRequest): UserDataResponse {
        return api.resetUserUpdatePassword(body = resetUserUpdatePasswordRequest)
    }
}