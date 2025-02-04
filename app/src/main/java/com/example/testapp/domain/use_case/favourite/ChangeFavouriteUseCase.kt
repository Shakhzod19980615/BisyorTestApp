package com.example.testapp.domain.use_case.favourite

import com.example.testapp.data.request.favourite.ChangeFavoriteStatusRequest
import com.example.testapp.domain.model.ChangeFavouriteModel
import com.example.testapp.domain.repository.favourite.FavouriteRepository
import javax.inject.Inject

class ChangeFavouriteUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {
    suspend fun invoke(param: ChangeFavoriteStatusRequest) : ChangeFavouriteModel {
        return favouriteRepository.likeItem(param)
    }
    suspend fun getUserFavoriteIds():List<Int> {
        return favouriteRepository.getUserFavoriteIds()
    }
}