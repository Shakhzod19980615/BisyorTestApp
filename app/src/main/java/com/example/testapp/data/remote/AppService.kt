package com.example.testapp.data.remote

import com.example.testapp.data.remote.dto.announcementItemDetails.AnnouncementItemDetailsDto
import com.example.testapp.data.remote.dto.announcementList.AnnouncementListDto
import com.example.testapp.data.remote.dto.categoryTab.CategoryDtoItem
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {

    @GET("items/main-category-list")
    suspend fun getAllMainCategories(): List<CategoryDtoItem>

    @GET("search/category-items")
    suspend fun getAnnouncementList(
        @Query("cat_id") categoryId: Int,
    ): AnnouncementListDto

    @GET("items/items-card")
    suspend fun getAnnouncementDetails(
        @Query("id") itemId: Int
    ): AnnouncementItemDetailsDto

    //TODO: - Favorites Section
    /*@GET("favorites/favorites-list")
    suspend fun getFavouriteItems(
        @Query("lang") lang: String,
        @Query("page") offset: Int
    ): Response<AnnouncementListResponse>

    @GET("favorites/favorites-list-id")
    suspend fun getUserFavoriteIds(): Response<List<Int>>*/
/*
    @POST("favorites/change-favorites")
    suspend fun likeItem(@Body body: ChangeFavoriteStatusRequest): Response<ChangeFavoriteStatusResponse>

    @POST("favorites/favorites-set-list")
    suspend fun saveUnAuthUserFavorites(@Body body: UploadUnAuthUserFavoritesRequest): Response<ResponseBody>*/
}