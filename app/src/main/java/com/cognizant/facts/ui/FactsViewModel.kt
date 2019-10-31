package com.cognizant.facts.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognizant.facts.data.FactsRepository
import androidx.lifecycle.viewModelScope
import com.cognizant.facts.data.DataState
import kotlinx.coroutines.launch
import javax.inject.Inject

class FactsViewModel @Inject constructor (private val factsRepository: FactsRepository) : ViewModel() {

    private val factsDataState: MutableLiveData<DataState>? = MutableLiveData()

    //Exposing live data to view and not Mutable live status
    fun getCountryDataState(): LiveData<DataState>? = factsDataState

    fun getCountryFacts(forceUpdate : Boolean = false) {
        if(!forceUpdate &&
            factsDataState?.value is DataState.Success
            && (factsDataState.value as DataState.Success).countryData != null) return

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

@Suppress("UNCHECKED_CAST")
class FactsViewModelFactory @Inject constructor (private val viewModel: FactsViewModel) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FactsViewModel::class.java)) {
            viewModel as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}