package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel

interface VerifyCodeRepository {
    suspend fun verifyCode(verificationCodeRequest: VerificationCodeRequest):UserDataModel
}