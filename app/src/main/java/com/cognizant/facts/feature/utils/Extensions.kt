package com.cognizant.facts.feature.utils

import com.cognizant.facts.feature.ui.FactsAdapter.Companion.FactItemWithNoDescription
import com.cognizant.facts.feature.ui.FactsAdapter.Companion.FactItemWithNoImage
import com.cognizant.facts.feature.data.model.Fact

/**
 * Extension function to remove empty items(with null title) and decide item type based on null description or image
 */
fun List<Fact>.mapItemType(): List<Fact> {
    //Remove empty items with null title
    val list = filter { fact ->
        fact.title?.isNotEmpty() ?: true
    }

    //Decide item type based on null description or image
    list.forEach { fact ->
        if (fact.description.isNullOrEmpty())
            fact.itemType = FactItemWithNoDescription
        if (fact.image.isNullOrEmpty())
            fact.itemType = FactItemWithNoImage
    }
    return list
}

