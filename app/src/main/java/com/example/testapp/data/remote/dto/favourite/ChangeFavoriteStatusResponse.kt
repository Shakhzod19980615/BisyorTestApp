package com.example.testapp.data.remote.dto.favourite

import com.example.testapp.domain.model.ChangeFavouriteModel

data class ChangeFavoriteStatusResponse(
    val status :Int,
    val message :String,
    val favoritesList :List<Int>
)
fun ChangeFavoriteStatusResponse.toChangeFavouriteModel() = ChangeFavouriteModel(
    status = status.toString(),
    message = message,
    favouriteList = favoritesList
)
