package com.cognizant.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cognizant.news.R
import com.cognizant.news.data.model.Article
import kotlinx.android.synthetic.main.news_activity.*

// Currently there is only one Fragment to navigate in App. In Future can have additional Fragments
enum class FragmentName {
    NewsDetails
}

class NewsActivity : AppCompatActivity(), NewsHomeFragment.OnFragmentInteractionListener {

    private lateinit var mCurrentFragment: Fragment

    private lateinit var newsHomeFragment: NewsHomeFragment

    private lateinit var newsDetailFragment: NewsDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_activity)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            newsHomeFragment = NewsHomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, newsHomeFragment)
                .addToBackStack(null)
                .commit()
            mCurrentFragment = newsHomeFragment
        }
    }

    override fun onNavigation(fragmentDetailsPair: Pair<FragmentName, Article?>) {
        when (fragmentDetailsPair.first.ordinal) {

            //navigate to NewsDetailsFragment
            FragmentName.NewsDetails.ordinal -> {
                newsDetailFragment = NewsDetailFragment()
                newsDetailFragment.apply {
                    arguments = Bundle().apply {
                        putParcelable(NEWS_PARAM, fragmentDetailsPair.second)
                        mCurrentFragment = newsDetailFragment
                        supportActionBar?.title = fragmentDetailsPair.second?.title
                    }
                }
            }
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.container, mCurrentFragment)
            .addToBackStack(null)
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        if (mCurrentFragment is NewsDetailFragment) {
            //Pop NewsDetails Fragment
            supportFragmentManager.popBackStackImmediate()
            setCurrentFragmentToHome()
        } else {
            finish()
        }
    }

    private fun setCurrentFragmentToHome(){
        mCurrentFragment = newsHomeFragment
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
