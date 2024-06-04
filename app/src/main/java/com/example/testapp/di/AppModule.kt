package com.example.testapp.di
import com.example.testapp.common.Constants
import com.example.testapp.data.remote.AppService
import com.example.testapp.data.repository.announcement.AnnouncementItemRepositoryImpl
import com.example.testapp.data.repository.announcementDetails.AnnouncementDetailsRepositoryImpl
import com.example.testapp.data.repository.authoration.SignUpRepositoryImpl
import com.example.testapp.data.repository.authoration.VerifyCodeRepositoryImpl
import com.example.testapp.data.repository.categoryTab.CategoryTabRepositoryImpl
import com.example.testapp.domain.repository.announcement.AnnouncementItemRepository
import com.example.testapp.domain.repository.announcementItemDetails.AnnouncementDetailsRepository
import com.example.testapp.domain.repository.authoration.SignUpRepository
import com.example.testapp.domain.repository.authoration.VerifyCodeRepository
import com.example.testapp.domain.repository.categoryTab.CategoryTabRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): AppService {
        return retrofit.create(AppService::class.java)
    }
    @Provides
    @Singleton
    fun provideCategoryRepository(api : AppService): CategoryTabRepository {
        return CategoryTabRepositoryImpl(api) // Replace with your actual implementation
    }

    @Provides
    @Singleton
    fun provideAnnouncementItemRepository(api : AppService): AnnouncementItemRepository {
        return AnnouncementItemRepositoryImpl(api) // Replace with your actual implementation
    }
    @Provides
    @Singleton
    fun provideAnnouncementDetailRepository(api: AppService): AnnouncementDetailsRepository{
        return AnnouncementDetailsRepositoryImpl(api)
    }
    @Provides
    @Singleton
    fun provideSignUpRepository(api: AppService) : SignUpRepository {
        return SignUpRepositoryImpl(api)
    }
    @Provides
    @Singleton
    fun provideVerifyCodeRepository(api: AppService) : VerifyCodeRepository {
        return VerifyCodeRepositoryImpl(api)
    }
  /*  @Provides
    @Singleton
    fun provideCoinRepository(api: CoinPaprikaApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }*/
}