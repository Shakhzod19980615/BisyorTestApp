package com.example.testapp.data.repository.authoration

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.request.resetUserConfirmRequest.ResetUserConfirmRequest
import com.example.testapp.domain.repository.authoration.ResetUserConfirmRepository
import retrofit2.Response
import javax.inject.Inject

class ResetUserConfirmRepositoryImpl @Inject constructor(
    private  val api : AppService
) : ResetUserConfirmRepository {
    override suspend fun resetUserConfirm(resetUserConfirmRequest: ResetUserConfirmRequest): Result<Boolean> {
        return try {
            val response = api.resetUserVerify(resetUserConfirmRequest)
            if (response.isSuccessful) {
                Result.success(response.body() ?: false)
            } else {
                Result.failure(Exception("API call failed with error: ${response.message()}"))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
    }
}