package com.cognizant.facts.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cognizant.facts.data.model.Fact
import kotlinx.android.synthetic.main.facts_home_fragment.*
import com.cognizant.facts.R
import com.cognizant.facts.data.DataState
import com.cognizant.facts.databinding.FactsHomeFragmentBinding
import com.cognizant.facts.utils.NetworkUtility
import com.cognizant.facts.utils.mapItemType
import com.cognizant.facts.utils.showSnackBar
import kotlinx.android.synthetic.main.no_connection.*
import javax.inject.Inject
import androidx.recyclerview.widget.LinearLayoutManager
import com.cognizant.facts.di.DaggerFactsComponent
import com.cognizant.facts.utils.isHomeFragment

/**
 * Fragment to display list of facts data in recycler view.
 */
class FactsHomeFragment : Fragment() {

    @Inject
    lateinit var factsViewModelFactory: FactsViewModelFactory

    lateinit var factsViewModel: FactsViewModel

    lateinit var factsAdapter: FactsAdapter

    // This is the instance of our parent activity's interface to update from fragment
    private var mListener: OnFragmentInteractionListener? = null

    //Handle list item click
    var itemClick: (Fact?) -> (Unit) = { fact ->
        mListener?.navigationToDetailFragment(fact)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerFactsComponent
            .builder()
            .build()
            .inject(this)
        factsViewModel =
            ViewModelProviders.of(this, factsViewModelFactory).get(FactsViewModel::class.java)
        factsViewModel.getCountryFacts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Data binding
        val binding: FactsHomeFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.facts_home_fragment, container, false)
        val view = binding.root
        binding.factsViewModel = factsViewModel
        binding.lifecycleOwner = activity
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factsAdapter = FactsAdapter(itemClick)
        setupRecyclerView()
        setUpSwipeToRefresh()
        setUpNetworkListener()
        fetchFacts()
    }

    private fun fetchFacts() {
        factsViewModel.getCountryDataState()
            ?.observe(viewLifecycleOwner, Observer { factsDataStatus ->
                when (factsDataStatus) {

                    is DataState.Success -> {
                        if ((activity as AppCompatActivity).isHomeFragment()) {
                            mListener?.setToolBarHomeTitle(
                                factsDataStatus.countryData?.title ?: ""
                            )
                        }
                        factsAdapter.factList =
                            factsDataStatus.countryData?.factList?.mapItemType()
                        factsAdapter.notifyDataSetChanged()
                        swipeRefresh.isRefreshing = false
                    }

                    is DataState.Error -> {
                        homeFragmentContainer.showSnackBar(factsDataStatus.error.errorMessage)
                    }
                }
            })
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.apply {
            layoutManager = linearLayoutManager
            factsAdapter = FactsAdapter(itemClick)
            adapter = factsAdapter
        }
    }

    private fun setUpSwipeToRefresh() {
        swipeRefresh.setOnRefreshListener {
            factsViewModel.getCountryFacts(true)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context

        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * Define the methods to update parent Activity.
     */
    interface OnFragmentInteractionListener {
        fun navigationToDetailFragment(fact: Fact?)

        fun setToolBarHomeTitle(title: String)
    }

    private fun setUpNetworkListener() {
        NetworkUtility.registerNetworkCallback()
        NetworkUtility.observe(this, Observer { connection ->
            recyclerView.visibility = if (connection) View.VISIBLE else View.GONE
            noConnectionLayout.visibility = if (connection) View.GONE else View.VISIBLE
            swipeRefresh.isEnabled = connection
        })
        tryAgainBtn.setOnClickListener {
            factsViewModel.getCountryFacts(true)
        }
    }

}
