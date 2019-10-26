package com.cognizant.news.api

import com.cognizant.news.utils.Constants.Companion.ERROR_MESSAGE
import com.google.gson.JsonParser
import retrofit2.HttpException

class ErrorResponse constructor(throwable: Throwable) {

    private var errorMessage: String

    init {
        errorMessage = when (throwable) {
            is HttpException -> {
                val errorJsonString = throwable.response()?.errorBody()?.string()
                JsonParser()
                    .parse(errorJsonString)
                    .asJsonObject["message"]
                    .toString()
            }
            else -> throwable.message ?: ERROR_MESSAGE
        }
    }
}