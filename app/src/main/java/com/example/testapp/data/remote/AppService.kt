package com.example.testapp.data.remote

import ChatListResponse
import com.example.testapp.data.remote.dto.announcementItemDetails.AnnouncementItemDetailsDto
import com.example.testapp.data.remote.dto.announcementList.AnnouncementListDto
import com.example.testapp.data.remote.dto.authoration.UserDataResponse
import com.example.testapp.data.remote.dto.basicResponse.BasicResponseDto
import com.example.testapp.data.remote.dto.basicResponse.SendChatSuccessResponse
import com.example.testapp.data.remote.dto.categoryTab.CategoryDtoItem
import com.example.testapp.data.remote.dto.chat.CreateChatResponse
import com.example.testapp.data.remote.dto.chat.MessageResponseDto
import com.example.testapp.data.remote.dto.createAnnouncement.AnnouncementDynamicResponse
import com.example.testapp.data.remote.dto.favourite.ChangeFavoriteStatusResponse
import com.example.testapp.data.remote.dto.favourite.UserSubscriptionListResponse
import com.example.testapp.data.remote.dto.searchCategory.CategoryResponseDto
import com.example.testapp.data.request.RegistrationRequest
import com.example.testapp.data.request.chat.CreateChatRequest
import com.example.testapp.data.request.chat.SendTextMessageRequest
import com.example.testapp.data.request.createAnnouncement.AnnouncementPropertiesRequest
import com.example.testapp.data.request.favourite.ChangeFavoriteStatusRequest
import com.example.testapp.data.request.login.LoginRequest
import com.example.testapp.data.request.login.RegisterWithSocialRequest
import com.example.testapp.data.request.resetPassword.ResetUserUpdatePasswordRequest
import com.example.testapp.data.request.resetUser.ResetUserRequest
import com.example.testapp.data.request.resetUserConfirmRequest.ResetUserConfirmRequest
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppService {
    @GET("items/last-items")
    suspend fun getItems(
        @Query("lang") lang: String,
        @Query("page") offset: Int
    ): AnnouncementListDto

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
    @POST("favorites/change-favorites/")
    suspend fun likeItem(@Body body: ChangeFavoriteStatusRequest): ChangeFavoriteStatusResponse
    @GET("favorites/favorites-list-id")
    suspend fun getUserFavoriteIds(): List<Int>

    @GET("favorites/favorites-list")
    suspend fun getFavouriteItems(
        @Query("lang") lang: String,
        @Query("page") offset: Int
    ): AnnouncementListDto
    @GET("profile/subscriptions-items-list")
    suspend fun getUserSubscriptions(
        @Query("lang") lang: String,
        @Query("page") page: Int
    ): UserSubscriptionListResponse

    /* @POST("favorites/favorites-set-list")
    suspend fun saveUnAuthUserFavorites(@Body body: UploadUnAuthUserFavoritesRequest): Response<ResponseBody>
*/
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

    //Search Section
    @GET("items/child-categories")
    suspend fun getActiveInsideCategories(
        @Query("lang") lang: String,
        @Query("category_id") categoryId: Int?
    ): List<CategoryResponseDto>
    @POST("items/get-item-fields")
    suspend fun getDynamicItemsByCategoryId(@Body body: AnnouncementPropertiesRequest):
           List<AnnouncementDynamicResponse>
    @GET("items/child-category-list")
    suspend fun getAllInsideCategories(
        @Query("lang") lang: String,
        @Query("category_id") categoryId: Int?
    ):List<CategoryResponseDto>
    @GET("search/category-items")
    suspend fun getItemsByCategory(
        @Query("lang") lang: String,
        @Query("page") offset: Int,
        @Query("cat_id") categoryId: Int,
        @Query("sorting") sorting: String? = null
    ): AnnouncementListDto

    @GET("search/global-search-items")
    suspend fun getItemsByQuery(
        @Query("lang") lang: String,
        @Query("page") offset: Int,
        @Query("text") query: String,
        @Query("sorting") sorting: String? = null
    ): AnnouncementListDto


    //TODO: - MESSAGING SECTION

    @POST("items-chats/create-items-chats")
    suspend fun createChatByAnnouncement(@Body body: CreateChatRequest): CreateChatResponse

    @GET("chats/chats-all-list")
    suspend fun getAllChats(
        @Query("lang") lang: String,
        @Query("page") offset: Int
    ): ChatListResponse

    @GET("chats/admin-users-list")
    suspend fun getAdminChats(
        @Query("lang") lang: String,
        @Query("page") offset: Int
    ):ChatListResponse

    @GET("items-chats/shops-users-list")
    suspend fun getStoreChats(
        @Query("lang") lang: String,
        @Query("page") offset: Int
    ): ChatListResponse

    @GET("items-chats/items-users-list")
    suspend fun getAllAnnouncementChats(
        @Query("lang") lang: String,
        @Query("page") offset: Int
    ): ChatListResponse

    @GET("items-chats/personal-item-message-list")
    suspend fun getAnnouncementChatsById(
        @Query("lang") lang: String,
        @Query("page") offset: Int,
        @Query("item_id") id: Int
    ): Response<ChatListResponse>
    @GET("chats/active-chats")
    suspend fun getUserMessages(
        @Query("lang") lang: String,
        @Query("chat_id") id: Int,
        @Query("page") offset: Int
    ): MessageResponseDto

    @POST("chats/send-message")
    suspend fun sendMessageToUser(@Body body: SendTextMessageRequest): SendChatSuccessResponse

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