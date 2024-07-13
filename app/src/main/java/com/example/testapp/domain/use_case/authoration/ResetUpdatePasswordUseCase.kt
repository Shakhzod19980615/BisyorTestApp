package com.example.testapp.domain.use_case.authoration

import com.example.testapp.data.remote.dto.authoration.toUserDataModel
import com.example.testapp.data.request.resetPassword.ResetUserUpdatePasswordRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.repository.authoration.ResetUpdatePasswordRepository
import javax.inject.Inject

class ResetUpdatePasswordUseCase @Inject constructor(
    private  val resetUpdatePasswordRepository: ResetUpdatePasswordRepository
) {

    suspend fun invoke(resetUserUpdatePasswordRequest: ResetUserUpdatePasswordRequest): UserDataModel {
        return resetUpdatePasswordRepository.updatePassword(resetUserUpdatePasswordRequest = resetUserUpdatePasswordRequest).toUserDataModel()
    }
}