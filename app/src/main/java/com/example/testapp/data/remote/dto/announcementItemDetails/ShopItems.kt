package com.example.testapp.data.remote.dto.announcementItemDetails

import com.example.testapp.data.remote.dto.announcementList.AnnouncementListDto

data class ShopItems(
    val hasNextPage: Boolean,
    val itemCount: Int,
    val items: AnnouncementListDto,
    val totalCount: Int
)