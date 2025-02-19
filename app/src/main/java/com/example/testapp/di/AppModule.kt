package com.example.testapp.di
import com.example.testapp.common.Constants
import com.example.testapp.common.ErrorParser
import com.example.testapp.data.remote.AppService
import com.example.testapp.data.repositoryImpl.announcement.AnnouncementItemRepositoryImpl
import com.example.testapp.data.repositoryImpl.announcementDetails.AnnouncementDetailsRepositoryImpl
import com.example.testapp.data.repositoryImpl.authoration.LoginRepositoryImpl
import com.example.testapp.data.repositoryImpl.authoration.ResetPasswordRepositoryImpl
import com.example.testapp.data.repositoryImpl.authoration.ResetUserConfirmRepositoryImpl
import com.example.testapp.data.repositoryImpl.authoration.ResetUserRepositoryImpl
import com.example.testapp.data.repositoryImpl.authoration.SignUpRepositoryImpl
import com.example.testapp.data.repositoryImpl.authoration.VerifyCodeRepositoryImpl
import com.example.testapp.data.repositoryImpl.categoryTab.CategoryTabRepositoryImpl
import com.example.testapp.data.repositoryImpl.chat.ChatRepositoryImpl
import com.example.testapp.data.repositoryImpl.createAnnouncement.CreateAnnouncementRepositoryImpl
import com.example.testapp.data.repositoryImpl.favourite.FavouriteRepositoryImp
import com.example.testapp.data.repositoryImpl.searchRepository.SearchRepositoryImpl
import com.example.testapp.domain.repository.announcement.AnnouncementItemRepository
import com.example.testapp.domain.repository.announcementItemDetails.AnnouncementDetailsRepository
import com.example.testapp.domain.repository.authoration.LoginRepository
import com.example.testapp.domain.repository.authoration.ResetUpdatePasswordRepository
import com.example.testapp.domain.repository.authoration.ResetUserConfirmRepository
import com.example.testapp.domain.repository.authoration.ResetUserRepository
import com.example.testapp.domain.repository.authoration.SignUpRepository
import com.example.testapp.domain.repository.authoration.VerifyCodeRepository
import com.example.testapp.domain.repository.categoryTab.CategoryTabRepository
import com.example.testapp.domain.repository.chat.ChatRepository
import com.example.testapp.domain.repository.createAnnouncement.CreateAnnouncementRepository
import com.example.testapp.domain.repository.favourite.FavouriteRepository
import com.example.testapp.domain.repository.searchRepository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.demo.dana.di.authInterceptor.AuthInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(provideOkhttp())// set HTTP client that Retrofit will use to make requests
            .addConverterFactory(GsonConverterFactory.create())//to specify how Retrofit should convert
            // JSON responses from the server into Java/Kotlin objects
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): AppService {
        return retrofit.create(AppService::class.java)
        // will generate the necessary code to make network requests based on AppService interface.
    }

    @Singleton
    @Provides
    fun provideOkhttp(): OkHttpClient {
        val okHttpClient =
            OkHttpClient.Builder()//build an OkHttp client which is a powerful HTTP client
        okHttpClient.writeTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES)
            //set write timeouts for POST and read timeouts for GET
            .addInterceptor(AuthInterceptor())
        return okHttpClient.build()

    }

    @Provides
    @Singleton
    fun provideCategoryRepository(api: AppService): CategoryTabRepository {
        return CategoryTabRepositoryImpl(api) // Replace with your actual implementation
    }

    @Provides
    @Singleton
    fun provideAnnouncementItemRepository(api: AppService): AnnouncementItemRepository {
        return AnnouncementItemRepositoryImpl(api) // Replace with your actual implementation
    }

    @Provides
    @Singleton
    fun provideAnnouncementDetailRepository(api: AppService): AnnouncementDetailsRepository {
        return AnnouncementDetailsRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideSignUpRepository(api: AppService): SignUpRepository {
        return SignUpRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideVerifyCodeRepository(api: AppService): VerifyCodeRepository {
        return VerifyCodeRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(api: AppService): LoginRepository {
        return LoginRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideResetUserRepository(api: AppService): ResetUserRepository {
        return ResetUserRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideConfirmUserRestoreRepository(api: AppService): ResetUserConfirmRepository {
        return ResetUserConfirmRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideConfirmResetPasswordRepository(api: AppService): ResetUpdatePasswordRepository {
        return ResetPasswordRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(api: AppService): SearchRepository {
        return SearchRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAnnouncementRepository(api: AppService): CreateAnnouncementRepository {
        return CreateAnnouncementRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideChatRepository(api: AppService): ChatRepository {
        return ChatRepositoryImpl(api)
    }
   @Provides
    @Singleton
    fun provideChangeFavouriteStatusRepository(api: AppService): FavouriteRepository {
        return FavouriteRepositoryImp(api)
    }
    @Provides
    @Singleton
    fun provideErrorParser(): ErrorParser {
        return ErrorParser()
    }
  /*  @Provides
    @Singleton
    fun provideCoinRepository(api: CoinPaprikaApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }*/
}