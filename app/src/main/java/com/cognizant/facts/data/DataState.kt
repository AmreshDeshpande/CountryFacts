package com.cognizant.facts.data

import com.cognizant.facts.data.api.ErrorResponse
import com.cognizant.facts.data.model.Country

sealed class DataState {

    data class Success(val countryData: Country?) : DataState()

    data class Error(val error: ErrorResponse) : DataState()

    object Loading : DataState()
}