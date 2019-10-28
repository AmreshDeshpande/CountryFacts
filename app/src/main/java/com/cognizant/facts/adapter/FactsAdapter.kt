package com.cognizant.facts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cognizant.facts.data.model.Fact
import com.cognizant.facts.data.model.Country
import kotlinx.android.synthetic.main.facts_item.view.*
import com.cognizant.facts.R
import com.squareup.picasso.Picasso


class FactsAdapter(private val itemClick: (Fact?) -> (Unit)) :
    RecyclerView.Adapter<FactsItemViewHolder>() {

    var countryData: Country? = null

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): FactsItemViewHolder {
        val itemView =
            LayoutInflater.from(parentView.context).inflate(R.layout.facts_item, parentView, false)
        return FactsItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return countryData?.factList?.size ?: 0
    }

    override fun onBindViewHolder(factsItemViewHolder: FactsItemViewHolder, position: Int) {
        val article = countryData?.factList?.get(position)
        factsItemViewHolder.bind(article, itemClick)
    }

}

class FactsItemViewHolder(newItemView: View) : RecyclerView.ViewHolder(newItemView) {

    fun bind(fact: Fact?, itemClick: (Fact?) -> (Unit)) {
        itemView
            .also { view ->
                view.title.text = fact?.title
                  view.content.text = fact?.description
            }.also { view ->
                fact?.image?.let { url ->
                    Picasso.with(view.context)
                        .load(fact?.image)
                        .into(view.factImage)
                }

            }.also { view ->
                view.setOnClickListener {
                    itemClick.invoke(fact)
                }
            }
    }
}
