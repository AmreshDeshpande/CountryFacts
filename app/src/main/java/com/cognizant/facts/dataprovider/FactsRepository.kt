package com.cognizant.facts.dataprovider

import com.cognizant.facts.data.api.ErrorResponse
import com.cognizant.facts.data.model.Country

/**
 * Provider of [Country].
 * This interface abstracts the logic of fetching Country through the API or other data sources.
 */
interface FactsRepository {

    suspend fun getFacts(success: (Country?) -> (Unit), failure: (ErrorResponse) -> (Unit))
}