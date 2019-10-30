package com.cognizant.facts.di

import com.cognizant.facts.data.api.FactsApiService
import com.cognizant.facts.data.FactsRepositoryImpl
import com.cognizant.facts.data.FactsRepository
import com.cognizant.facts.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class FactsModule {
    @Provides
    fun providesRepository(factsApiService: FactsApiService): FactsRepository {
        return FactsRepositoryImpl(factsApiService)
    }

    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesFactsApiService(retrofit: Retrofit): FactsApiService {
        return retrofit.create(FactsApiService::class.java)
    }
}