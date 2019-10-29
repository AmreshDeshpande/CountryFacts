package com.cognizant.facts.feature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognizant.facts.feature.dataprovider.FactsRepository
import androidx.lifecycle.viewModelScope
import com.cognizant.facts.feature.data.DataState
import kotlinx.coroutines.launch

class FactsViewModel(private val factsRepository: FactsRepository) : ViewModel() {

    private val factsDataState: MutableLiveData<DataState>? = MutableLiveData()

    //Exposing live data to view and not Mutable live status
    fun getCountryDataState(): LiveData<DataState>? = factsDataState

    fun getCountry() {
        factsDataState?.value = DataState.Loading
        viewModelScope.launch {
            factsRepository.getFacts(
                success = { countryData ->
                    factsDataState?.value = DataState.Success(countryData)
                },
                failure = { error ->
                    factsDataState?.value = DataState.Error(error)
                }
            )
        }
    }
}

class FactsViewModelFactory(private val factsRepository: FactsRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FactsViewModel::class.java)) {
            FactsViewModel(this.factsRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}