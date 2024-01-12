package com.example.testapp.data.remote.dto.announcementList

data class AnnouncementListDto(
    val hasNextPage: Boolean,
    val itemCount: Int,
    val items: List<AnnouncementItemDto>,
    val totalCount: Int
)