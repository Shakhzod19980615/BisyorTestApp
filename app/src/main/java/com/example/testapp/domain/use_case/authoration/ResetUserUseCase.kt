package com.example.testapp.domain.use_case.authoration

import com.example.testapp.data.request.resetUser.ResetUserRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel
import com.example.testapp.domain.repository.authoration.ResetUserRepository
import javax.inject.Inject

class ResetUserUseCase @Inject constructor(
    private val repository: ResetUserRepository
) {

    suspend fun invoke(resetUserRequest: ResetUserRequest): BasicResponseModel {
        return repository.resetUser(resetUserRequest = resetUserRequest)
    }
}