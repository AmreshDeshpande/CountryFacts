package com.cognizant.facts.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MenuItem
import com.cognizant.facts.R
import com.cognizant.facts.data.model.Fact
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.facts_detail_fragment.*

const val FACTS_PARAM = "facts_param"

class FactsDetailFragment: Fragment() {
    private var param: Fact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getParcelable(FACTS_PARAM)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindFactDetails(param)
    }

    private fun bindFactDetails(fact: Fact?) {
        factDetailImage.let { imageView ->
            Picasso.with(activity)
                .load(fact?.image)
                .error(R.drawable.ic_placeholder_canada)
                .into(imageView)
        }
        factDetailContent.text = fact?.description
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.facts_detail_fragment, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}



