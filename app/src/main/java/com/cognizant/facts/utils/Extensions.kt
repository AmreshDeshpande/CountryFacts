package com.cognizant.facts.utils

import com.cognizant.facts.adapter.FactsAdapter.Companion.FactItemWithNoDescription
import com.cognizant.facts.adapter.FactsAdapter.Companion.FactItemWithNoImage
import com.cognizant.facts.data.model.Fact

/**
 * Extension function to remove empty items(with null title) and decide item type based on null description or image
 */
fun List<Fact>.mapItemType() : List<Fact>
{
    //Remove empty items with null title
    val list = filter { fact ->
        fact.title?.isNotEmpty()?:false
    }

    //Decide item type based on null description or image
    list.forEach { fact ->
        if(fact.description.isNullOrEmpty())
            fact.itemType = FactItemWithNoDescription
        if(fact.image.isNullOrEmpty())
            fact.itemType = FactItemWithNoImage
    }
    return list
}

