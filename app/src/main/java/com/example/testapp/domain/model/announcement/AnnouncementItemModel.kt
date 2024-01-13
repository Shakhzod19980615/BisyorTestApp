package com.example.testapp.domain.model.announcement

data class AnnouncementItemModel(
    val description: String,
    val favorites: Boolean,
    val id: Int,
    val img_m: String,
    val img_s: String,
    val old_price: Any?,
    val price: String,
    val serviceFixed: Boolean,
    val serviceMarked: Boolean,
    val servicePremimum: Boolean,
    val serviceQuick: Boolean,
    val serviceUp: Boolean,
    val shopId: Any?,
    val title: String,
    val userId: Int,
    val verified: Boolean
)
