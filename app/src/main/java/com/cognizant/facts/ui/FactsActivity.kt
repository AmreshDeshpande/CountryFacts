package com.cognizant.facts.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cognizant.facts.R
import com.cognizant.facts.data.model.Fact
import kotlinx.android.synthetic.main.facts_activity.*

// Currently there is only one Fragment to navigate in App. In Future can have additional Fragments
enum class FragmentName {
    FactsDetails
}

class FactsActivity : AppCompatActivity(), FactsHomeFragment.OnFragmentInteractionListener {

    private lateinit var factsHomeFragment: FactsHomeFragment

    private lateinit var factsDetailFragment: FactsDetailFragment

    private var homeTitle :String? = null

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
        }else{
            val title = savedInstanceState.getString("title")
            setActionBarTitle(title)
            if(supportFragmentManager.backStackEntryCount > 1){
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
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
                        //title = fragmentDetailsPair.second?.title
                        supportActionBar?.title = fragmentDetailsPair.second?.title
                    }
                }
            }
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.container, factsDetailFragment)
            .addToBackStack(null)
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun setActionBarTitle(title: String) {
        homeTitle = title
        supportActionBar?.title = title
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 1){
            //Pop FactsDetails Fragment
            supportFragmentManager.popBackStackImmediate()
            setCurrentFragmentToHome()
        }else{
            finish()
        }
    }

    private fun setCurrentFragmentToHome(){
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = homeTitle
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString("title", homeTitle)
    }
}
