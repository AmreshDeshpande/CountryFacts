package com.cognizant.facts.data

import com.cognizant.facts.data.api.ErrorResponse
import com.cognizant.facts.data.api.FactsApiService
import com.cognizant.facts.data.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FactsRepositoryImpl(private val factsApiService: FactsApiService) :
    FactsRepository {

    override suspend fun getFacts(
        success: (Country?) -> (Unit),
        failure: (ErrorResponse) -> (Unit)
    ) {
        try {
            val response = withContext(Dispatchers.IO) {
                factsApiService.getFacts()
            }
                if (response.isSuccessful) {
                    success.invoke(response.body())
                } else {
                    failure.invoke(ErrorResponse(Throwable()))
                }

        } catch (exception: Exception) {
            failure.invoke(ErrorResponse(exception))
        }
    }

}