package com.example.testapp.domain.repository.authoration

import com.example.testapp.data.request.resetUserConfirmRequest.ResetUserConfirmRequest
import retrofit2.Response

interface ResetUserConfirmRepository {
    suspend fun     resetUserConfirm(resetUserConfirmRequest: ResetUserConfirmRequest):Result<Boolean>
}