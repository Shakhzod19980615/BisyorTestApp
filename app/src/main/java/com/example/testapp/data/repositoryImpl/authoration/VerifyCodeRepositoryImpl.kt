package com.example.testapp.data.repositoryImpl.authoration

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.remote.dto.authoration.toUserDataModel
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.repository.authoration.VerifyCodeRepository
import javax.inject.Inject

class VerifyCodeRepositoryImpl @Inject constructor(
    private  val api: AppService
): VerifyCodeRepository {
    override suspend fun verifyCode(verificationCodeRequest: VerificationCodeRequest): UserDataModel {

        return try {
            api.verifySign(body = verificationCodeRequest).toUserDataModel()
        }catch (e:Exception){
            UserDataModel()
        }
    }
}