package com.example.testapp.data.remote.dto.announcementItemDetails

import com.google.gson.annotations.SerializedName

data class MainFields(
    val address: String,
    val categoryId: Int,
    val categoryName: String?,
    @SerializedName("coordinate_x")
    val coordinate_x: String,
    @SerializedName("coordinate_y")
    val coordinate_y: String,
    @SerializedName("date_cr")
    val date_cr: String?,
    val description: String,
    val districtName: String,
    @SerializedName("district_id")
    val district_id: Int,
    val favorites: Boolean,
    val id: Int?,
    val images: ArrayList<String>,
    @SerializedName("img_m")
    val img_m: String,
    @SerializedName("img_s")
    val img_s: String,
    @SerializedName("is_moderating")
    val is_moderating: Boolean,
    @SerializedName("is_publicated")
    val is_publicated: Boolean,
    val keyword: String,
    val link: String,
    @SerializedName("old_price")
    val old_price: Any?,
    val ownerType: Int,
    val ownerTypeName: String,
    val price: String?,
    val priceSearch: Int,
    @SerializedName("price_ex")
    val price_ex: Int,
    val recommend: String,
    @SerializedName("region_id")
    val region_id: Int,
    val serviceFixed: Boolean,
    val serviceMarked: Boolean,
    val servicePremimum: Boolean,
    val serviceQuick: Boolean,
    val serviceUp: Boolean,
    val shopId: Any,
    val shopName: String,
    val status: Int,
    val title: String,
    val totalCategoryName: String,
    @SerializedName("user_id")
    val user_id: Int,
    val verified: Boolean,
    val viewCount: Int?
)