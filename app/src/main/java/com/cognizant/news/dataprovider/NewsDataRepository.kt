package com.cognizant.news.dataprovider

import com.cognizant.news.api.ErrorResponse
import com.cognizant.news.data.model.News


/**
 * Provider of [News].
 * This interface abstracts the logic of fetching News through the API or other data sources.
 */
interface NewsDataRepository {

    suspend fun getNews(success: (News?) -> (Unit), failure: (ErrorResponse) -> (Unit))
}