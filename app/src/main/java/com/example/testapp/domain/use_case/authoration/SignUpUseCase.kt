package com.example.testapp.domain.use_case.authoration

import com.example.testapp.data.remote.dto.basicResponse.toBasicResponse
import com.example.testapp.data.request.RegistrationRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel
import com.example.testapp.domain.repository.authoration.SignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: SignUpRepository
)   {

    suspend fun invoke(registrationRequest: RegistrationRequest): BasicResponseModel {
        return repository.signUp(registrationRequest = registrationRequest).toBasicResponse()
    }
}