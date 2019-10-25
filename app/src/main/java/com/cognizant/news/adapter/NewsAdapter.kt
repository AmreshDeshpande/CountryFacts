package com.cognizant.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cognizant.news.R
import com.cognizant.news.data.model.News
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(private val itemClick: (News) -> (Unit)) : RecyclerView.Adapter<NewsItemViewHolder>(){

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): NewsItemViewHolder {
        val itemView = LayoutInflater.from(parentView.context).inflate(R.layout.news_item, parentView, false)
        return NewsItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return 5
    }

    override fun onBindViewHolder(newsItemViewHolder: NewsItemViewHolder, position: Int) {
        newsItemViewHolder.bind(itemClick)
    }

}

class NewsItemViewHolder(newItemView : View) : RecyclerView.ViewHolder(newItemView) {
    fun bind(itemClick: (News) -> (Unit)){
        itemView.also {view ->
            Picasso.with(view.context)
                .load("https://cdn.dnaindia.com/sites/default/files/styles/full/public/2018/06/20/695436-telstra.jpg")
                .fit()
                .centerCrop()
                .placeholder(android.R.drawable.ic_btn_speak_now)
                .into(itemView.newsImage)
        }.also { view ->
            view.setOnClickListener {
                itemClick.invoke(News("title","News"))
            }
        }
    }
}
