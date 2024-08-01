package com.example.testapp.data.repositoryImpl.authoration

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.request.login.LoginRequest
import com.example.testapp.data.request.login.RegisterWithSocialRequest
import com.example.testapp.domain.repository.authoration.LoginRepository
import javax.inject.Inject

 class LoginRepositoryImpl @Inject constructor(

    private val api : AppService
):LoginRepository {
    override suspend fun signIn(loginRequest: LoginRequest): UserDataResponse {
        return api.signIn(body = loginRequest)
    }

    override suspend fun registerWithSocial(registerWithSocialRequest: RegisterWithSocialRequest): UserDataResponse {
        return api.registerWithSocial(body = registerWithSocialRequest)
    }
}