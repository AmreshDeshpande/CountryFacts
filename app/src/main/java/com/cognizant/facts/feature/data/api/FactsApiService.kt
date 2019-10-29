package com.cognizant.facts.feature.data.api

import com.cognizant.facts.feature.data.model.Country
import retrofit2.Response
import retrofit2.http.GET

interface FactsApiService {

    @GET("/s/2iodh4vg0eortkl/facts.js")
    suspend fun getFacts(): Response<Country?>
}