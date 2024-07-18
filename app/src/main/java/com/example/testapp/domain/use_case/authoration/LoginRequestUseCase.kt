package com.example.testapp.domain.use_case.authoration

import com.example.testapp.data.remote.dto.authoration.toUserDataModel
import com.example.testapp.data.request.login.LoginRequest
import com.example.testapp.data.request.login.RegisterWithSocialRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.repository.authoration.LoginRepository
import javax.inject.Inject

class LoginRequestUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend fun invoke(loginRequest: LoginRequest): UserDataModel {
        return loginRepository.signIn(loginRequest = loginRequest).toUserDataModel()
    }

    suspend fun registerWithSocial(registerWithSocialRequest: RegisterWithSocialRequest): UserDataModel {
        return loginRepository.registerWithSocial(registerWithSocialRequest = registerWithSocialRequest)
            .toUserDataModel()
    }
}