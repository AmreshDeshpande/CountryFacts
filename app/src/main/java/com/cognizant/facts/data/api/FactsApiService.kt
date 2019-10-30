package com.cognizant.facts.data.api

import com.cognizant.facts.data.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface FactsApiService {

    @GET("/s/2iodh4vg0eortkl/facts.js")
    suspend fun getFacts(): Response<Country?>
}