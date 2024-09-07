package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.remote.dto.basicResponse.BasicResponseDto
import com.example.testapp.data.request.resetUser.ResetUserRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel

interface ResetUserRepository {

    suspend fun resetUser(resetUserRequest: ResetUserRequest): BasicResponseModel
}