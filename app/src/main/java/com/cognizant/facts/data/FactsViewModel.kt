package com.cognizant.facts.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognizant.facts.dataprovider.FactsDataRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FactsViewModel(private val factsDataRepository :FactsDataRepository) : ViewModel() {

    private val factsDataState: MutableLiveData<DataState>? = MutableLiveData()

    //Exposing live data to view and not Mutable live status
    fun getCountryDataState(): LiveData<DataState>? = factsDataState

    fun getCountry(){
        factsDataState?.value = DataState.Loading
        viewModelScope.launch {
            factsDataRepository.getFacts(
                success = { facts ->
                    facts
                    factsDataState?.value = DataState.Success(facts)
                },
                failure = { error ->
                    factsDataState?.value = DataState.Error(error)
                }
            )
        }
    }
}


class FactsViewModelFactory(private val factsDataRepository :FactsDataRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FactsViewModel::class.java)) {
            FactsViewModel(this.factsDataRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}