package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.request.login.LoginRequest
import com.example.testapp.data.request.login.RegisterWithSocialRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel

interface LoginRepository {
    suspend fun signIn(loginRequest: LoginRequest): UserDataModel
    suspend fun registerWithSocial(registerWithSocialRequest: RegisterWithSocialRequest): UserDataModel
}