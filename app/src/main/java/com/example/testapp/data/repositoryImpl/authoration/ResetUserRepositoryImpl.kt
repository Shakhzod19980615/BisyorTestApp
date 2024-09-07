package com.example.testapp.data.repositoryImpl.authoration

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.basicResponse.toBasicResponseModel
import com.example.testapp.data.request.resetUser.ResetUserRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel
import com.example.testapp.domain.repository.authoration.ResetUserRepository
import javax.inject.Inject

class ResetUserRepositoryImpl @Inject constructor(
    private val api : AppService
):ResetUserRepository {
    override suspend fun resetUser(resetUserRequest: ResetUserRequest): BasicResponseModel {
        return  try {
            api.resetUser(resetUserRequest).toBasicResponseModel()
        }catch (e:Exception){
            BasicResponseModel()
        }
    }
}