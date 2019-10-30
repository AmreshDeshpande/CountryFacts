package com.cognizant.facts.feature.dataprovider

import com.cognizant.facts.feature.data.api.ErrorResponse
import com.cognizant.facts.feature.data.model.Country

/**
 * Provider of [Country].
 * This interface abstracts the logic of fetching Country through the API or other data sources.
 */
interface FactsRepository {

    suspend fun getFacts(success: (Country?) -> (Unit), failure: (ErrorResponse) -> (Unit))
}