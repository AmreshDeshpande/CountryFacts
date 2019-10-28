package com.cognizant.facts.dataprovider

import com.cognizant.facts.api.ErrorResponse
import com.cognizant.facts.api.FactsApiService
import com.cognizant.facts.data.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class FactsApiDataProvider(private val factsApiService : FactsApiService) : FactsDataRepository {

     override suspend fun getFacts(success: (Country?) -> (Unit), failure: (ErrorResponse) -> (Unit)) {
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
         }
         catch (exception: Exception) {
             exception.printStackTrace()
         }
    }

}