package com.cognizant.news.dataprovider

import com.cognizant.news.api.ErrorResponse
import com.cognizant.news.api.NewsApiService
import com.cognizant.news.data.model.News
import com.cognizant.news.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiDataProvider : NewsDataRepository {


    private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .client(OkHttpClient())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    private val newsApiService =  retrofit.create(NewsApiService::class.java)

     override suspend fun getNews(success: (News?) -> (Unit), failure: (ErrorResponse) -> (Unit)) {
         try {
             val response = withContext(Dispatchers.IO) {
                 newsApiService.getNews()
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