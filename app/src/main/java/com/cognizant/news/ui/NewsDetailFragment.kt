package com.cognizant.news.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MenuItem
import com.cognizant.news.R
import com.cognizant.news.data.model.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_detail_fragment.*

const val NEWS_PARAM = "news_param"

class NewsDetailFragment: Fragment() {
    private var param: Article? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getParcelable(NEWS_PARAM)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindNewsDetails(param)
    }

    private fun bindNewsDetails(article: Article?) {
        newsDetailImage.let { imageView ->
            Picasso.with(activity)
                .load(article?.image)
                .into(newsDetailImage)
        }
        newsDetailContent.text = article?.content
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.news_detail_fragment, container, false)
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



