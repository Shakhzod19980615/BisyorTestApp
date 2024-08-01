package com.example.testapp.data.repositoryImpl.authoration

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.basicResponse.BasicResponseDto
import com.example.testapp.data.request.RegistrationRequest
import com.example.testapp.domain.repository.authoration.SignUpRepository
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(

    private val api : AppService
): SignUpRepository {
    override suspend fun signUp(registrationRequest: RegistrationRequest): BasicResponseDto {

        return api.signUp(body = registrationRequest)
    }

}