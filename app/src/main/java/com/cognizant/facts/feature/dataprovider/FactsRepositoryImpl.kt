package com.cognizant.facts.feature.dataprovider

import com.cognizant.facts.feature.data.api.ErrorResponse
import com.cognizant.facts.feature.data.api.FactsApiService
import com.cognizant.facts.feature.data.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class FactsRepositoryImpl(private val factsApiService: FactsApiService) : FactsRepository {

    override suspend fun getFacts(
        success: (Country?) -> (Unit),
        failure: (ErrorResponse) -> (Unit)
    ) {
        try {
            val response = withContext(Dispatchers.IO) {
                factsApiService.getFacts()
            }
            try {
                if (response.isSuccessful) {
                    success.invoke(response.body())
                } else {
                    failure.invoke(ErrorResponse(Throwable()))
                }
            } catch (throwable: Throwable) {
                failure.invoke(ErrorResponse(throwable))
            } catch (exception: HttpException) {
                failure.invoke(ErrorResponse(exception))
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

}