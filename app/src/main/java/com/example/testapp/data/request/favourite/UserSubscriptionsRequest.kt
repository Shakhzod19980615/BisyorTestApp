package com.example.testapp.data.request.favourite

import com.google.gson.annotations.SerializedName

data class UserSubscriptionsRequest(
    @SerializedName("lang")
    val lang: String,
    @SerializedName("page")
    val page: Int
)
