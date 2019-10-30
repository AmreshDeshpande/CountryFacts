package com.cognizant.facts

import android.app.Application
import com.cognizant.facts.utils.NetworkUtility

class FactsApplication : Application() {

    init {
        NetworkUtility.init(this)
    }
}