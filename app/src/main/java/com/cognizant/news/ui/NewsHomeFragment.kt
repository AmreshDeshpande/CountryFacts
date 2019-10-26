package com.cognizant.news.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.cognizant.news.adapter.NewsAdapter
import com.cognizant.news.data.NewsViewModel
import com.cognizant.news.data.NewsViewModelFactory
import com.cognizant.news.data.model.Article
import com.cognizant.news.dataprovider.NewsApiDataProvider
import kotlinx.android.synthetic.main.news_home_fragment.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cognizant.news.R


class NewsHomeFragment(private val navigation :(Pair<FragmentName, Article?>) -> (Unit) ) : Fragment() {

    private lateinit var viewModel: NewsViewModel

    private lateinit var newsAdapter :NewsAdapter

    //Handle item click
    private var itemClick: (Article?) -> (Unit) = { news ->
        navigation.invoke(Pair(FragmentName.NewsDetails, news))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.news_home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =  ViewModelProviders
            .of(this, NewsViewModelFactory(NewsApiDataProvider()))
            .get(NewsViewModel::class.java)
        fetchNews()
        setupRecyclerView()
        setUpSwipeToRefresh()
    }

    private fun fetchNews() {
        progressBar.visibility = View.VISIBLE
        viewModel.getNewsData()?.observe(viewLifecycleOwner, Observer { newsList ->
            newsAdapter.newsData = newsList
            newsAdapter.notifyDataSetChanged()
            swipeRefresh.isRefreshing = false
            progressBar.visibility = View.GONE
        })
        viewModel.getNews()
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
        progressBar.visibility = View.GONE
        swipeRefresh.setOnRefreshListener {
            fetchNews()
        }
    }

}
