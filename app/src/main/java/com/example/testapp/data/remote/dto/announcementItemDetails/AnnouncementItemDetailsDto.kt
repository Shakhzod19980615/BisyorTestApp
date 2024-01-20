package com.example.testapp.data.remote.dto.announcementItemDetails

import com.example.testapp.data.remote.dto.announcementList.AnnouncementListDto
import com.example.testapp.data.remote.dto.announcementList.toAnnouncementItem
import com.example.testapp.domain.model.announcementItemDetails.AnnouncementItemDetailsModel
import com.google.gson.annotations.SerializedName

data class AnnouncementItemDetailsDto(
    val card: AnnouncementItemDetails,
    val shopItems: AnnouncementListDto,
    @SerializedName("similar_blog")
    val similar_blog: AnnouncementListDto
)

fun AnnouncementItemDetailsDto.toAnnouncmentItemDetails() = AnnouncementItemDetailsModel(
    id = card.main_fields.id ?: -1,
    title = card.main_fields.title,
    price = card.main_fields.price?:"0.0",
    oldPrice = card.main_fields.old_price?:0.0,
    address = card.main_fields.address,
    categoryId = card.main_fields.categoryId,
    categoryName = card.main_fields.categoryName?:"Category",
    totalCategoryName = card.main_fields.totalCategoryName,
    districtId = card.main_fields.district_id,
    districtName = card.main_fields.districtName,
    coordinateX = card.main_fields.coordinate_x,
    coordinateY = card.main_fields.coordinate_y,
    description = card.main_fields.description,
    link = card.main_fields.link,
    date = card.main_fields.date_cr?:"12/12/2022",
    isFavorite = card.main_fields.favorites,
    viewedTotal = card.main_fields.viewCount?:0,
    imgCount = card.main_fields.images.size,
    images = card.main_fields.images,
    properties = card.dynamic_fields.map { it.toItemDynamicPropertyModel() },
    userId = card.user.userId,
    storeId = card.main_fields.shopId,
    storeName = card.main_fields.shopName,
    userName = card.user.name,
    userAvatar = card.user.avatar,
    phones = card.user.userPhone,
    isUserOnline = false,
    similarItems = similar_blog.items.map { it.toAnnouncementItem() },
    storeItems = shopItems.items.map { it.toAnnouncementItem() }

)