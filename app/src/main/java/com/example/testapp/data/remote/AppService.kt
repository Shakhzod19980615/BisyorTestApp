package com.example.testapp.data.remote

import com.example.testapp.data.remote.dto.CategoryDtoItem
import retrofit2.http.GET

interface AppService {

    @GET("items/main-category-list")
    suspend fun getAllMainCategories(): List<CategoryDtoItem>
}