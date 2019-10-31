package com.cognizant.facts.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cognizant.facts.R
import com.cognizant.facts.data.model.Fact
import com.cognizant.facts.utils.isHomeFragment
import kotlinx.android.synthetic.main.facts_activity.*

/**
 * Launch Activity to hold [FactsHomeFragment] and [FactsDetailFragment].
 * Handles fragments navigation
 */
class FactsActivity : AppCompatActivity(),
    FactsHomeFragment.OnFragmentInteractionListener {

    private var toolbarHomeTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.facts_activity)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            supportActionBar?.title = ""
            val factsHomeFragment = FactsHomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, factsHomeFragment)
                .addToBackStack(null)
                .commit()
        } else {
            toolbarHomeTitle = savedInstanceState.getString("title")
            if (isHomeFragment()) {
                setToolBarHomeTitle(title = toolbarHomeTitle ?: "")
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun navigationToDetailFragment(fact: Fact?) {
        val factsDetailFragment = FactsDetailFragment()
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
        toolbarHomeTitle = title
        supportActionBar?.title = title
    }

    override fun onBackPressed() {
        if (isHomeFragment()) {
            finish()
        } else {
            //Pop FactsDetails Fragment
            supportFragmentManager.popBackStackImmediate()
            setCurrentFragmentToHome()
        }
    }

    private fun setCurrentFragmentToHome() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = toolbarHomeTitle
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString("title", toolbarHomeTitle)
    }
}
