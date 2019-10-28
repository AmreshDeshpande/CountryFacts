package com.cognizant.facts.dataprovider

import com.cognizant.facts.api.ErrorResponse
import com.cognizant.facts.api.FactsApiService
import com.cognizant.facts.data.model.Country
import com.cognizant.facts.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FactsApiDataProvider : FactsDataRepository {


    private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .client(OkHttpClient())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    private val factsApiService =  retrofit.create(FactsApiService::class.java)

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