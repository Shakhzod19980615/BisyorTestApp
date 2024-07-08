package com.example.testapp.domain.use_case.authoration

import com.example.testapp.data.remote.dto.basicResponse.toBasicResponse
import com.example.testapp.data.request.resetPassword.ResetUserRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel
import com.example.testapp.domain.repository.authoration.ResetUserRepository
import javax.inject.Inject

class ResetUserUseCase @Inject constructor(
    private val repository: ResetUserRepository
) {

    suspend fun invoke(resetUserRequest: ResetUserRequest): BasicResponseModel {
        return repository.resetUser(resetUserRequest = resetUserRequest).toBasicResponse()
    }
}