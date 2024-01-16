package com.example.testapp.data.remote.dto.announcementItemDetails

data class SimilarBlog(
    val hasNextPage: Boolean,
    val itemCount: Int,
    val items: List<Any>,
    val totalCount: Int
)