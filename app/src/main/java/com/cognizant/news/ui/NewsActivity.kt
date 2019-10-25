package com.cognizant.news.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.cognizant.news.R
import com.cognizant.news.data.model.News
import kotlinx.android.synthetic.main.news_activity.*

// Currently there is only one Fragment to navigate in App. In Future can have additional Fragments
enum class FragmentName {
     NewsDetails
}

class NewsActivity : AppCompatActivity() {

    private lateinit var mCurrentFragment:Fragment

    private lateinit var newsHomeFragment : NewsHomeFragment

    private lateinit var newsDetailFragment : NewsDetailFragment

    private val navigation: (Pair<FragmentName, News>) -> (Unit)  = { pair ->

        when(pair.first.ordinal){

            //navigate to NewsDetailsFragment
            FragmentName.NewsDetails.ordinal ->{
                newsDetailFragment = NewsDetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(NEWS_PARAM, pair.second)
                    }
                }
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, newsDetailFragment)
                    .addToBackStack(null)
                    .commit()
                mCurrentFragment = newsDetailFragment
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_activity)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
             newsHomeFragment = NewsHomeFragment(navigation)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container,newsHomeFragment)
                .addToBackStack(null)
                .commit()
            mCurrentFragment = newsHomeFragment
        }

    }

    override fun onBackPressed() {
        if (mCurrentFragment is NewsDetailFragment) {
            supportFragmentManager.popBackStackImmediate()
            mCurrentFragment = newsHomeFragment
        } else {
            finish()
        }
    }

}
