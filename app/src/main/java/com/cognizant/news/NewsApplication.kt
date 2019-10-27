package com.cognizant.news

import android.app.Application
import com.cognizant.news.utils.NetworkUtility

class NewsApplication : Application() {

    init {
        NetworkUtility.init(this)
    }
}