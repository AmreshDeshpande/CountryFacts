package com.cognizant.news.api

import com.cognizant.news.data.model.News
import retrofit2.Response
import retrofit2.http.GET

interface NewsApiService {

    @GET("/v2/top-headlines?country=au&apiKey=0874bd9b761c44ad90edcc3eee57dca1")
    suspend fun getNews(): Response<News?>
}