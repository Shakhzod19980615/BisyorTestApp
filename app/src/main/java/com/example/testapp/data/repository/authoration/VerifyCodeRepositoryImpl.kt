package com.example.testapp.data.repository.authoration

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.authoration.VerifyCodeResponseDto
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.repository.authoration.VerifyCodeRepository
import retrofit2.Response
import javax.inject.Inject

class VerifyCodeRepositoryImpl @Inject constructor(
    private  val api: AppService
): VerifyCodeRepository {
    override suspend fun verifyCode(verificationCodeRequest: VerificationCodeRequest): VerifyCodeResponseDto {

        return api.verifySign(body = verificationCodeRequest)
    }
}