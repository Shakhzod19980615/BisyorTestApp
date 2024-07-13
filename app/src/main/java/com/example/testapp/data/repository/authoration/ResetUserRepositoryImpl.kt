package com.example.testapp.data.repository.authoration

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.basicResponse.BasicResponseDto
import com.example.testapp.data.request.resetUser.ResetUserRequest
import com.example.testapp.domain.repository.authoration.ResetUserRepository
import javax.inject.Inject

class ResetUserRepositoryImpl @Inject constructor(
    private val api : AppService
):ResetUserRepository {
    override suspend fun resetUser(resetUserRequest: ResetUserRequest): BasicResponseDto {
        return  api.resetUser(resetUserRequest)
    }
}