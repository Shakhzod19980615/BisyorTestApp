package com.example.testapp.domain.model

data class ChangeFavouriteModel(
    val status: String,
    val message: String,
    val favouriteList: List<Int>
)