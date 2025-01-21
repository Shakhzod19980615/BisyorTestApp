package com.example.testapp.data.request.favourite

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class ChangeFavoriteStatusRequest(
    @SerializedName("lang")
    val lang: String,
    @SerializedName("item_id")
    val itemId: Int,
)
