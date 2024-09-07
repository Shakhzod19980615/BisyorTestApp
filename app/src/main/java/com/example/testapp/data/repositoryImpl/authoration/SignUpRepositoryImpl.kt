package com.example.testapp.data.repositoryImpl.authoration

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.basicResponse.BasicResponseDto
import com.example.testapp.data.remote.dto.basicResponse.toBasicResponseModel
import com.example.testapp.data.request.RegistrationRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel
import com.example.testapp.domain.repository.authoration.SignUpRepository
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(

    private val api : AppService
): SignUpRepository {
    override suspend fun signUp(registrationRequest: RegistrationRequest): BasicResponseModel {

        return try {
            api.signUp(body = registrationRequest).toBasicResponseModel()
        }catch (e:Exception){
            BasicResponseModel()
        }
    }

}