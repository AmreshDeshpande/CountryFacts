package com.cognizant.facts.feature.data

import com.cognizant.facts.feature.data.api.ErrorResponse
import com.cognizant.facts.feature.data.model.Country

sealed class DataState {

    data class Success(val countryData: Country?) : DataState()

    data class Error(val error: ErrorResponse) : DataState()

    object Loading : DataState()
}