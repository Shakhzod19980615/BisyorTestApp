package com.example.testapp.domain.use_case.authoration

import com.example.testapp.data.request.resetUserConfirmRequest.ResetUserConfirmRequest
import com.example.testapp.domain.repository.authoration.ResetUserConfirmRepository
import retrofit2.Response
import javax.inject.Inject

class ResetUserConfirmUseCase @Inject constructor(

    private val repository: ResetUserConfirmRepository
) {

    suspend fun invoke(resetUserConfirmRequest: ResetUserConfirmRequest): Boolean {
        return repository.resetUserConfirm(resetUserConfirmRequest).getOrElse { false }
    }
}