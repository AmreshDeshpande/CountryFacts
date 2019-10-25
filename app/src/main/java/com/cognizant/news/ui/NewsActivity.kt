package com.cognizant.news.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cognizant.news.R

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewsFragment.newInstance())
                .commitNow()
        }
    }

}
