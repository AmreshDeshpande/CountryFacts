package com.cognizant.news.ui

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cognizant.news.adapter.NewsAdapter
import com.cognizant.news.data.NewsViewModel
import com.cognizant.news.data.NewsViewModelFactory
import com.cognizant.news.data.model.Article
import com.cognizant.news.dataprovider.NewsApiDataProvider
import kotlinx.android.synthetic.main.news_home_fragment.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cognizant.news.R
import com.cognizant.news.data.DataState
import com.cognizant.news.databinding.NewsHomeFragmentBinding
import com.cognizant.news.utils.NetworkUtility
import com.cognizant.news.utils.showSnackBar
import kotlinx.android.synthetic.main.no_connection.*

class NewsHomeFragment : Fragment() {

    // This is the instance of our parent activity's interface that we define here
    private var mListener: OnFragmentInteractionListener? = null

    private lateinit var newsViewModel: NewsViewModel

    private lateinit var newsAdapter :NewsAdapter

    //Handle list item click
    private var itemClick: (Article?) -> (Unit) = { news ->
        mListener?.onNavigation(Pair(FragmentName.NewsDetails, news))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newsViewModel =  ViewModelProviders
            .of(this, NewsViewModelFactory(NewsApiDataProvider()))
            .get(NewsViewModel::class.java)
        //Data binding
        val binding: NewsHomeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.news_home_fragment,container,false)
        val view = binding.root
        binding.newsViewModel = newsViewModel
        binding.lifecycleOwner = activity
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fetchNews()
        setupRecyclerView()
        setUpSwipeToRefresh()
        setUpNetworkListener()
    }

    private fun fetchNews() {
        newsViewModel.getNewsData()?.observe(viewLifecycleOwner, Observer { newsDataStatus ->
            when (newsDataStatus) {

                is DataState.Success -> {
                    newsAdapter.newsData = newsDataStatus.newsData
                    newsAdapter.notifyDataSetChanged()
                    swipeRefresh.isRefreshing = false
                }
                is DataState.Error -> {
                    homeFragmentContainer.showSnackBar(newsDataStatus.error.errorMessage)
                }
            }
        })
        newsViewModel.getNews()
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        recyclerView.apply {
           layoutManager = linearLayoutManager
            newsAdapter = NewsAdapter(itemClick)
            adapter =  newsAdapter
        }
    }

    private fun setUpSwipeToRefresh() {
       // progressBar.visibility = View.GONE
        swipeRefresh.setOnRefreshListener {
            fetchNews()
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
        fun onNavigation(fragmentDetailsPair : Pair<FragmentName, Article?>)
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
                    //progressBar.visibility = View.GONE
                    noConnectionLayout.visibility = View.GONE
                }
            }
        })

        tryAgainBtn.setOnClickListener {
            newsViewModel.getNews()
        }
    }

}
