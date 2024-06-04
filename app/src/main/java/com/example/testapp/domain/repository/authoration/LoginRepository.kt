package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.request.login.LoginRequest

interface LoginRepository {
    suspend fun signIn(loginRequest: LoginRequest): UserDataResponse
}