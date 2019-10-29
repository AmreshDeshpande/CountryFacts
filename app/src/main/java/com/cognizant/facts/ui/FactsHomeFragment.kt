package com.cognizant.facts.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cognizant.facts.adapter.FactsAdapter
import com.cognizant.facts.data.FactsViewModel
import com.cognizant.facts.data.model.Fact
import kotlinx.android.synthetic.main.facts_home_fragment.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cognizant.facts.R
import com.cognizant.facts.data.DataState
import com.cognizant.facts.databinding.FactsHomeFragmentBinding
import com.cognizant.facts.di.DaggerFactsComponent
import com.cognizant.facts.di.FactsModule
import com.cognizant.facts.utils.NetworkUtility
import com.cognizant.facts.utils.mapItemType
import com.cognizant.facts.utils.showSnackBar
import kotlinx.android.synthetic.main.no_connection.*
import javax.inject.Inject

class FactsHomeFragment : Fragment() {

    @Inject
    lateinit var factsViewModel: FactsViewModel

    @Inject
     lateinit var factsAdapter :FactsAdapter

    // This is the instance of our parent activity's interface to update from fragment
    private var mListener: OnFragmentInteractionListener? = null


    //Handle list item click
     var itemClick: (Fact?) -> (Unit) = { fact ->
        mListener?.onNavigation(Pair(FragmentName.FactsDetails, fact))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerFactsComponent
            .builder()
            .factsModule(FactsModule(this))
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Data binding
        val binding: FactsHomeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.facts_home_fragment,container,false)
        val view = binding.root
        binding.factsViewModel = factsViewModel
        binding.lifecycleOwner = activity
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fetchFacts()
        setupRecyclerView()
        setUpSwipeToRefresh()
        setUpNetworkListener()
    }

    private fun fetchFacts() {
        factsViewModel.getCountryDataState()?.observe(viewLifecycleOwner, Observer { factsDataStatus ->
            when (factsDataStatus) {

                is DataState.Success -> {
                    mListener?.setActionBarTitle(factsDataStatus.countryData?.title?:"")
                    factsAdapter.factList = factsDataStatus.countryData?.factList?.mapItemType()
                    factsAdapter.notifyDataSetChanged()
                    swipeRefresh.isRefreshing = false
                }
                is DataState.Error -> {
                    homeFragmentContainer.showSnackBar(factsDataStatus.error.errorMessage)
                }
            }
        })
        factsViewModel.getCountry()
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        recyclerView.apply {
           layoutManager = linearLayoutManager
            factsAdapter = FactsAdapter(itemClick)
            adapter =  factsAdapter
        }
    }

    private fun setUpSwipeToRefresh() {
        swipeRefresh.setOnRefreshListener {
            fetchFacts()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context

        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
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
        fun onNavigation(fragmentDetailsPair : Pair<FragmentName, Fact?>)

        fun setActionBarTitle(title: String)
    }

    private fun setUpNetworkListener() {

        NetworkUtility.registerNetworkCallback()
        NetworkUtility.observe(this, Observer { connection ->
            connection?.let {
                if (!connection) {
                    recyclerView.visibility = View.GONE
                    noConnectionLayout.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    noConnectionLayout.visibility = View.GONE
                }
            }
        })

        tryAgainBtn.setOnClickListener {
            factsViewModel.getCountry()
        }
    }

}
