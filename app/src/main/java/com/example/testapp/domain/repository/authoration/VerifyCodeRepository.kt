package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest

interface VerifyCodeRepository {
    suspend fun verifyCode(verificationCodeRequest: VerificationCodeRequest):UserDataResponse
}