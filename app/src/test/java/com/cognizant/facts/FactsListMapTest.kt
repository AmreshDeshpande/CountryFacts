package com.cognizant.facts

import com.cognizant.facts.data.model.Fact
import com.cognizant.facts.ui.FactsAdapter.Companion.FactItem
import com.cognizant.facts.ui.FactsAdapter.Companion.FactItemWithNoDescription
import com.cognizant.facts.ui.FactsAdapter.Companion.FactItemWithNoImage
import com.cognizant.facts.utils.mapItemType
import junit.framework.Assert.assertTrue
import org.junit.Test

class FactsListMapTest {

    @Test
    fun testEmptyFactsTitle() {
        //Given
        val emptyFactsTitleList = listOf(
            Fact(
                "",
                "description",
                image = ("url")
            ),
            Fact(
                null,
                "description",
                image = ("url")
            )
        )

        //When
        val mappedList = emptyFactsTitleList.mapItemType()

        //Then
        assertTrue(mappedList.isEmpty())
    }

    @Test
    fun testNoDescriptionFacts() {
        //Given
        val noDescriptionFactsTitleList = listOf(
            Fact(
                "title",
                "",
                image = ("url")
            ),
            Fact(
                "title",
                null,
                image = ("url")
            )
        )

        //When
        val mappedList = noDescriptionFactsTitleList.mapItemType()

        //Then
        assertTrue(mappedList.size == 2)
        assertTrue(mappedList[0].itemType == FactItemWithNoDescription)
        assertTrue(mappedList[1].itemType == FactItemWithNoDescription)
    }

    @Test
    fun testNoImageFacts() {
        //Given
        val noImageFactsTitleList = listOf(
            Fact(
                "title",
                "description",
                image = ("")
            ),
            Fact(
                "title",
                "description",
                image = (null)
            )
        )

        //When
        val mappedList = noImageFactsTitleList.mapItemType()

        //Then
        assertTrue(mappedList.size == 2)
        assertTrue(mappedList[0].itemType == FactItemWithNoImage)
        assertTrue(mappedList[1].itemType == FactItemWithNoImage)
    }

    @Test
    fun testFactsWithAllData() {
        //Given
        val factsList = listOf(
            Fact(
                "title",
                "description",
                image = ("url")
            )
        )

        //When
        val mappedList = factsList.mapItemType()

        //Then
        assertTrue(mappedList.size == 1)
        assertTrue(mappedList[0].itemType == FactItem)
    }

}