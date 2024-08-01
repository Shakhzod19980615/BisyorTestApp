package com.example.testapp.data.repositoryImpl.authoration

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.request.resetPassword.ResetUserUpdatePasswordRequest
import com.example.testapp.domain.repository.authoration.ResetUpdatePasswordRepository
import javax.inject.Inject

class ResetPasswordRepositoryImpl @Inject constructor(

    private val api : AppService
): ResetUpdatePasswordRepository {
    override suspend fun updatePassword(resetUserUpdatePasswordRequest: ResetUserUpdatePasswordRequest): UserDataResponse {
        return api.resetUserUpdatePassword(body = resetUserUpdatePasswordRequest)
    }
}