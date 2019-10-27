package com.cognizant.news.data

import com.cognizant.news.api.ErrorResponse
import com.cognizant.news.data.model.News

sealed class DataState {

    data class Success(val newsData: News?) : DataState()

    data class Error(val error: ErrorResponse) : DataState()

    object Loading : DataState()
}