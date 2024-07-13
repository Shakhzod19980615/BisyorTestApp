package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.remote.dto.basicResponse.BasicResponseDto
import com.example.testapp.data.request.resetUser.ResetUserRequest

interface ResetUserRepository {

    suspend fun resetUser(resetUserRequest: ResetUserRequest): BasicResponseDto
}