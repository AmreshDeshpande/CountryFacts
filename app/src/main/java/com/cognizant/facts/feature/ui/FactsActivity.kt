package com.cognizant.facts.feature.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cognizant.facts.R
import com.cognizant.facts.feature.data.model.Fact
import kotlinx.android.synthetic.main.facts_activity.*

class FactsActivity : AppCompatActivity(), FactsHomeFragment.OnFragmentInteractionListener {

    private lateinit var factsHomeFragment: FactsHomeFragment

    private lateinit var factsDetailFragment: FactsDetailFragment

    private var homeTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.facts_activity)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportActionBar?.title = ""
            factsHomeFragment = FactsHomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, factsHomeFragment)
                .addToBackStack(null)
                .commit()
        } else {
            homeTitle = savedInstanceState.getString("title")
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                setToolBarHomeTitle(title = homeTitle ?: "")
            }
        }
    }

    override fun navigationToDetailFragment(fact: Fact?) {
        factsDetailFragment = FactsDetailFragment()
        factsDetailFragment.apply {
            arguments = Bundle().apply {
                putParcelable(FACTS_PARAM, fact)
            }
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.container, factsDetailFragment)
            .addToBackStack(null)
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun setToolBarHomeTitle(title: String) {
        homeTitle = title
        supportActionBar?.title = title
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            //Pop FactsDetails Fragment
            supportFragmentManager.popBackStackImmediate()
            setCurrentFragmentToHome()
        } else {
            finish()
        }
    }

    private fun setCurrentFragmentToHome() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = homeTitle
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString("title", homeTitle)
    }
}
