package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.remote.dto.basicResponse.BasicResponseDto
import com.example.testapp.data.request.RegistrationRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel

interface SignUpRepository {
    suspend fun signUp(registrationRequest: RegistrationRequest): BasicResponseModel
}