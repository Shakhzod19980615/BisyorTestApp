package com.example.testapp.di
import com.example.testapp.common.Constants
import com.example.testapp.data.remote.AppService
import com.example.testapp.data.repository.CategoryRepositoryImpl
import com.example.testapp.domain.repository.CategoryRepository
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
    fun provideCategoryRepository(api : AppService): CategoryRepository {
        return CategoryRepositoryImpl(api) // Replace with your actual implementation
    }
  /*  @Provides
    @Singleton
    fun provideCoinRepository(api: CoinPaprikaApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }*/
}