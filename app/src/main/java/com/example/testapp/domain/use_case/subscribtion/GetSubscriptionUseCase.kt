package com.example.testapp.domain.use_case.subscribtion

import com.example.testapp.data.request.favourite.UserSubscriptionsRequest
import com.example.testapp.domain.model.userDataModel.SubscribedUserModel
import com.example.testapp.domain.repository.favourite.FavouriteRepository
import javax.inject.Inject

class GetSubscriptionUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    suspend fun getSubscribedUsers(param: UserSubscriptionsRequest): List<SubscribedUserModel> {
        return repository.getSubscribedUsers(param = param)
    }
}