package com.cognizant.facts.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cognizant.facts.R
import com.cognizant.facts.data.model.Fact
import kotlinx.android.synthetic.main.facts_activity.*

// Currently there is only one Fragment to navigate in App. In Future can have additional Fragments
enum class FragmentName {
    FactsDetails
}

class FactsActivity : AppCompatActivity(), FactsHomeFragment.OnFragmentInteractionListener {

    private lateinit var mCurrentFragment: Fragment

    private lateinit var factsHomeFragment: FactsHomeFragment

    private lateinit var factsDetailFragment: FactsDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.facts_activity)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            factsHomeFragment = FactsHomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, factsHomeFragment)
                .addToBackStack(null)
                .commit()
            mCurrentFragment = factsHomeFragment
        }
    }

    override fun onNavigation(fragmentDetailsPair: Pair<FragmentName, Fact?>) {
        when (fragmentDetailsPair.first.ordinal) {

            //navigate to FactsDetailsFragment
            FragmentName.FactsDetails.ordinal -> {
                factsDetailFragment = FactsDetailFragment()
                factsDetailFragment.apply {
                    arguments = Bundle().apply {
                        putParcelable(FACTS_PARAM, fragmentDetailsPair.second)
                        mCurrentFragment = factsDetailFragment
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
        if (mCurrentFragment is FactsDetailFragment) {
            //Pop FactsDetails Fragment
            supportFragmentManager.popBackStackImmediate()
            setCurrentFragmentToHome()
        } else {
            finish()
        }
    }

    private fun setCurrentFragmentToHome(){
        mCurrentFragment = factsHomeFragment
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
