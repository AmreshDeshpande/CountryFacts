package com.cognizant.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cognizant.news.data.model.Article
import com.cognizant.news.data.model.News
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_item.view.*
import com.cognizant.news.R


class NewsAdapter(private val itemClick: (Article?) -> (Unit)) :
    RecyclerView.Adapter<NewsItemViewHolder>() {

    var newsData: News? = null

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): NewsItemViewHolder {
        val itemView =
            LayoutInflater.from(parentView.context).inflate(R.layout.news_item, parentView, false)
        return NewsItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return newsData?.newsList?.size ?: 0
    }

    override fun onBindViewHolder(newsItemViewHolder: NewsItemViewHolder, position: Int) {
        val article = newsData?.newsList?.get(position)
        newsItemViewHolder.bind(article, itemClick)
    }

}

class NewsItemViewHolder(newItemView: View) : RecyclerView.ViewHolder(newItemView) {

    fun bind(article: Article?, itemClick: (Article?) -> (Unit)) {
        itemView
            .also { view ->
                view.title.text = article?.title?: itemView.context.getString(R.string.news_content_unavailable)
                view.content.text = article?.content
            }.also { view ->
                Picasso.with(view.context)
                    .load(article?.image)
                    .error(R.drawable.ic_news_placeholder)
                    .into(itemView.newsImage)
            }.also { view ->
                view.setOnClickListener {
                    itemClick.invoke(article)
                }
            }
    }
}
