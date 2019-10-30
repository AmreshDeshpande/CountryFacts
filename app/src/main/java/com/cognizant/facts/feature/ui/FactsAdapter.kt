package com.cognizant.facts.feature.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cognizant.facts.feature.data.model.Fact
import kotlinx.android.synthetic.main.facts_item.view.*
import com.cognizant.facts.R
import com.squareup.picasso.Picasso

class FactsAdapter(private val itemClick: (Fact?) -> (Unit)) :
    RecyclerView.Adapter<FactsAdapter.BaseViewHolder<Fact?>>() {

    companion object {
        const val FactItem = 0
        const val FactItemWithNoDescription = 1
        const val FactItemWithNoImage = 2
    }

    var factList: List<Fact>? = null

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): BaseViewHolder<Fact?> {

        return when (viewType) {
            FactItem -> {
                val itemView =
                    LayoutInflater.from(parentView.context)
                        .inflate(R.layout.facts_item, parentView, false)
                FactsItemViewHolder(itemView)
            }

            FactItemWithNoDescription -> {
                val itemView =
                    LayoutInflater.from(parentView.context)
                        .inflate(R.layout.facts_item_no_description, parentView, false)
                FactsItemNoDescriptionViewHolder(itemView)
            }

            FactItemWithNoImage -> {
                val itemView =
                    LayoutInflater.from(parentView.context)
                        .inflate(R.layout.facts_item_no_image, parentView, false)
                FactsItemNoImageViewHolder(itemView)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return factList?.size ?: 0
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Fact?>, position: Int) {
        val article = factList?.get(position)
        holder.bind(article, itemClick)
    }

    override fun getItemViewType(position: Int): Int {
        return factList?.get(position)?.itemType
            ?: throw IllegalArgumentException("Invalid view type")
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, itemClick: (Fact?) -> (Unit))
    }

    inner class FactsItemViewHolder(newItemView: View) : BaseViewHolder<Fact?>(newItemView) {
        override fun bind(fact: Fact?, itemClick: (Fact?) -> Unit) {
            itemView.apply {
                title.text = fact?.title
                description.text = fact?.description
                fact?.image?.let { url ->
                    Picasso.with(context)
                        .load(url)
                        .fit().centerInside()
                        .error(R.drawable.place_holder)
                        .into(factImage)
                    setOnClickListener {
                        itemClick.invoke(fact)
                    }
                }
            }
        }
    }

    inner class FactsItemNoDescriptionViewHolder(newItemView: View) :
        BaseViewHolder<Fact?>(newItemView) {
        override fun bind(fact: Fact?, itemClick: (Fact?) -> Unit) {
            itemView.apply {
                title.text = fact?.title
                fact?.image?.let { url ->
                    Picasso.with(context)
                        .load(url)
                         .error(R.drawable.place_holder)
                        .into(factImage)
                }
                setOnClickListener {
                    itemClick.invoke(fact)
                }

            }
        }
    }

    inner class FactsItemNoImageViewHolder(newItemView: View) :
        BaseViewHolder<Fact?>(newItemView) {
        override fun bind(fact: Fact?, itemClick: (Fact?) -> Unit) {
            itemView.apply {
                title.text = fact?.title
                description.text = fact?.description
                setOnClickListener {
                    itemClick.invoke(fact)
                }
            }
        }
    }
}




