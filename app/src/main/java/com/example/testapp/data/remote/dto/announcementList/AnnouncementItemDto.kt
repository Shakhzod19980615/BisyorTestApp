package com.example.testapp.data.remote.dto.announcementList

import com.example.testapp.domain.model.announcement.AnnouncementItemModel

data class AnnouncementItemDto(
    val description: String,
    val favorites: Boolean,
    val id: Int,
    val img_m: String,
    val img_s: String,
    val old_price: Any,
    val price: String,
    val serviceFixed: Boolean,
    val serviceMarked: Boolean,
    val servicePremimum: Boolean,
    val serviceQuick: Boolean,
    val serviceUp: Boolean,
    val shopId: Any,
    val title: String,
    val userId: Int,
    val verified: Boolean
)

fun AnnouncementItemDto.toAnnouncementItem() = AnnouncementItemModel(
    description = description,
    favorites = favorites,
    id = id,
    img_m = img_m,
    img_s = img_s,
    old_price = old_price,
    price = price,
    serviceFixed = serviceFixed,
    serviceMarked = serviceMarked,
    servicePremimum = servicePremimum,
    serviceQuick = serviceQuick,
    serviceUp = serviceUp,
    shopId = shopId,
    title = title,
    userId = userId,
    verified = verified
)