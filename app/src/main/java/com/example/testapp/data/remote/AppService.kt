package com.example.testapp.data.remote

import com.example.testapp.data.remote.dto.announcementItemDetails.AnnouncementItemDetailsDto
import com.example.testapp.data.remote.dto.announcementList.AnnouncementListDto
import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.remote.dto.basicResponse.BasicResponseDto
import com.example.testapp.data.remote.dto.categoryTab.CategoryDtoItem
import com.example.testapp.data.request.RegistrationRequest
import com.example.testapp.data.request.login.LoginRequest
import com.example.testapp.data.request.login.RegisterWithSocialRequest
import com.example.testapp.data.request.resetPassword.ResetUserUpdatePasswordRequest
import com.example.testapp.data.request.resetUser.ResetUserRequest
import com.example.testapp.data.request.resetUserConfirmRequest.ResetUserConfirmRequest
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("login/registration")
    suspend fun signUp(@Body body: RegistrationRequest):BasicResponseDto
    @POST("login/send-code")
    suspend fun verifySign(@Body body: VerificationCodeRequest): UserDataResponse
    @POST("login/login")
    suspend fun signIn(@Body body: LoginRequest): UserDataResponse
    @POST("login/get-code")
    suspend fun resetUser(@Body body: ResetUserRequest): BasicResponseDto

    @POST("login/check-code")
    suspend fun resetUserVerify(@Body body: ResetUserConfirmRequest): Response<Boolean>
    @POST("login/reset-password")
    suspend fun resetUserUpdatePassword(@Body body: ResetUserUpdatePasswordRequest): UserDataResponse
    @POST(value = "account/login-social-set")
    suspend fun registerWithSocial(@Body body: RegisterWithSocialRequest): UserDataResponse

    /*//TODO: - Authorization section
    @POST("login/login")
    suspend fun signIn(@Body body: LoginRequest): Response<UserDataModel>

    @POST("login/registration")
    suspend fun signUp(@Body body: RegistrationRequest): Response<ResponseBody>

    @POST("login/send-code")
    suspend fun verifySign(@Body body: CheckCodeRequest): Response<UserDataModel>

    @POST("login/get-code")
    suspend fun resetUser(@Body body: ResetUserRequest): Response<ResponseBody>

    @POST("login/check-code")
    suspend fun resetUserVerify(@Body body: ResetUserConfirmRequest): Response<Boolean>

    @POST("login/reset-password")
    suspend fun resetUserUpdatePassword(@Body body: ResetUserUpdatePasswordRequest): Response<UserDataModel>

    @GET(value = "https://login.yandex.ru/info")
    suspend fun yandexData(@Query("oauth_token") token: String): Response<YandexUserDataResponse>

    @POST(value = "account/login-social-set")
    suspend fun registerWithSocial(@Body body: RegisterWithSocialRequest): Response<UserDataModel>*/

}