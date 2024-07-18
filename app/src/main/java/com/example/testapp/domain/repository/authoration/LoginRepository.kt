package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.request.login.LoginRequest
import com.example.testapp.data.request.login.RegisterWithSocialRequest

interface LoginRepository {
    suspend fun signIn(loginRequest: LoginRequest): UserDataResponse
    suspend fun registerWithSocial(registerWithSocialRequest: RegisterWithSocialRequest): UserDataResponse
}