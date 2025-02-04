package com.example.testapp.domain.repository.favourite

import com.example.testapp.data.request.favourite.ChangeFavoriteStatusRequest
import com.example.testapp.data.request.favourite.UserSubscriptionsRequest
import com.example.testapp.domain.model.ChangeFavouriteModel
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.model.userDataModel.SubscribedUserModel

interface FavouriteRepository {
    suspend fun likeItem(param: ChangeFavoriteStatusRequest): ChangeFavouriteModel
    suspend fun getUserFavoriteIds(): List<Int>
    suspend fun getFavouriteItems(lang:String,page:Int): List<AnnouncementItemModel>
    suspend fun getSubscribedUsers(param: UserSubscriptionsRequest): List<SubscribedUserModel>
}