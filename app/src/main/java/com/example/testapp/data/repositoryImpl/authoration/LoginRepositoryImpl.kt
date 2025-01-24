package com.example.testapp.data.repositoryImpl.authoration

import com.example.testapp.common.MySettings
import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.remote.dto.authoration.toUserDataModel
import com.example.testapp.data.request.login.LoginRequest
import com.example.testapp.data.request.login.RegisterWithSocialRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.repository.authoration.LoginRepository
import javax.inject.Inject

 class LoginRepositoryImpl @Inject constructor(

    private val api : AppService
):LoginRepository {
    override suspend fun signIn(loginRequest: LoginRequest): UserDataModel {
        return try {
           val result= api.signIn(body = loginRequest)
            MySettings.setToken(result.token)
            result.toUserDataModel()
        }catch (
            e:Exception
        ){
            UserDataModel()
        }
    }

    override suspend fun registerWithSocial(registerWithSocialRequest: RegisterWithSocialRequest): UserDataModel {
        return try {
            api.registerWithSocial(body = registerWithSocialRequest).toUserDataModel()
        }catch (e:Exception){
            UserDataModel()
        }
    }
}