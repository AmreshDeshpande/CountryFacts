package com.cognizant.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cognizant.news.R

class NewsAdapter : RecyclerView.Adapter<NewsItemViewHolder>(){

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): NewsItemViewHolder {
        val itemView = LayoutInflater.from(parentView.context).inflate(R.layout.news_item, parentView, false)
        return NewsItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return 5
    }

    override fun onBindViewHolder(newsItemViewHolder: NewsItemViewHolder, position: Int) {
    }

}

class NewsItemViewHolder(newItemView : View) : RecyclerView.ViewHolder(newItemView) {



}
