package com.cognizant.facts.di

import androidx.lifecycle.ViewModelProviders
import com.cognizant.facts.adapter.FactsAdapter
import com.cognizant.facts.api.FactsApiService
import com.cognizant.facts.data.FactsViewModel
import com.cognizant.facts.data.FactsViewModelFactory
import com.cognizant.facts.dataprovider.FactsApiDataProvider
import com.cognizant.facts.dataprovider.FactsDataRepository
import com.cognizant.facts.ui.FactsHomeFragment
import com.cognizant.facts.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class FactsModule(private val factsHomeFragment: FactsHomeFragment)
{
    @Provides
    fun providesRepository(factsApiService: FactsApiService): FactsDataRepository {
        return FactsApiDataProvider(factsApiService)
    }

    @Provides
    fun providesFactsViewModel(factsViewModelFactory: FactsViewModelFactory): FactsViewModel {
        return ViewModelProviders
            .of(factsHomeFragment, factsViewModelFactory)
            .get(FactsViewModel::class.java)
    }

    @Provides
    fun providesFactsViewModelFactory(factsDataRepository: FactsDataRepository): FactsViewModelFactory {
        return FactsViewModelFactory(factsDataRepository)
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