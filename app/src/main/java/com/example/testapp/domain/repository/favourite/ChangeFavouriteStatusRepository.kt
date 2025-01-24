package com.example.testapp.domain.repository.favourite

import com.example.testapp.data.remote.dto.favourite.ChangeFavoriteStatusResponse
import com.example.testapp.data.request.favourite.ChangeFavoriteStatusRequest
import com.example.testapp.domain.model.ChangeFavouriteModel

interface ChangeFavouriteStatusRepository {
    suspend fun likeItem(param: ChangeFavoriteStatusRequest): ChangeFavouriteModel
    suspend fun getUserFavoriteIds(): List<Int>
}