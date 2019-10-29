package com.cognizant.facts.feature.di

import androidx.lifecycle.ViewModelProviders
import com.cognizant.facts.feature.ui.FactsAdapter
import com.cognizant.facts.feature.data.api.FactsApiService
import com.cognizant.facts.feature.FactsViewModel
import com.cognizant.facts.feature.FactsViewModelFactory
import com.cognizant.facts.feature.dataprovider.FactsRepositoryImpl
import com.cognizant.facts.feature.dataprovider.FactsRepository
import com.cognizant.facts.feature.ui.FactsHomeFragment
import com.cognizant.facts.feature.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class FactsModule(private val factsHomeFragment: FactsHomeFragment) {
    @Provides
    fun providesRepository(factsApiService: FactsApiService): FactsRepository {
        return FactsRepositoryImpl(factsApiService)
    }

    @Provides
    fun providesFactsViewModel(factsViewModelFactory: FactsViewModelFactory): FactsViewModel {
        return ViewModelProviders
            .of(factsHomeFragment, factsViewModelFactory)
            .get(FactsViewModel::class.java)
    }

    @Provides
    fun providesFactsViewModelFactory(factsRepository: FactsRepository): FactsViewModelFactory {
        return FactsViewModelFactory(factsRepository)
    }

    @Provides
    fun providesFactsAdapter(): FactsAdapter {
        return FactsAdapter(factsHomeFragment.itemClick)
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