package com.example.testapp.domain.use_case.authoration

import com.example.testapp.data.remote.dto.authoration.toUserDataModel
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.repository.authoration.VerifyCodeRepository
import javax.inject.Inject

class VerifyCodeUseCase @Inject constructor(

    private val repository: VerifyCodeRepository
) {
    suspend fun invoke(verificationCodeRequest: VerificationCodeRequest): UserDataModel {
        return repository.verifyCode(verificationCodeRequest = verificationCodeRequest)
    }
}