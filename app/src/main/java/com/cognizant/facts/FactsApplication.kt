package com.cognizant.facts

import android.app.Application
import com.cognizant.facts.feature.utils.NetworkUtility

class FactsApplication : Application() {

    init {
        NetworkUtility.init(this)
    }
}