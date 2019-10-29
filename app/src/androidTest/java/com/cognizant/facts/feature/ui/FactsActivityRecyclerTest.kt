package com.cognizant.facts.feature.ui

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.cognizant.facts.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import com.cognizant.facts.R
import androidx.recyclerview.widget.RecyclerView
import androidx.test.annotation.UiThreadTest
import kotlinx.android.synthetic.main.facts_home_fragment.*
import org.hamcrest.Matchers
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.cognizant.facts.feature.data.DataState
import org.hamcrest.CoreMatchers.allOf

class FactsActivityRecyclerTest {
    @Rule
    @JvmField
    var mActivityTestRule: ActivityTestRule<FactsActivity> =
        object : ActivityTestRule<FactsActivity>(FactsActivity::class.java) {
            override fun afterActivityLaunched() {
                super.afterActivityLaunched()
                IdlingRegistry.getInstance()
                    .register(EspressoIdlingResource.mCountingIdlingResource)
                EspressoIdlingResource.increment()
            }
        }

    @Before
    @UiThreadTest
    fun setUp(){
        val factsHomeFragment: FactsHomeFragment =
            mActivityTestRule.activity.supportFragmentManager.fragments[0] as FactsHomeFragment
        factsHomeFragment.factsViewModel.getCountryDataState()?.observe(mActivityTestRule.activity, Observer {
            when (it) {
                is DataState.Success -> {
                    EspressoIdlingResource.decrement()
                }
                is DataState.Error -> {
                    EspressoIdlingResource.decrement()
                }
            }
        })
    }

    @Test
    fun testSampleRecyclerVisible() {
        onView(withId(R.id.recyclerView))
            .inRoot(
                RootMatchers.withDecorView(
                    Matchers.`is`(mActivityTestRule.activity.window.decorView)
                )
            ).check(matches(isDisplayed()))
    }

    @Test
    fun testCaseForRecyclerItemView() {
        onView(withText("About Canada")).check(matches(isDisplayed()))

        //Check title
        val textView = onView(
            allOf(
                withId(R.id.title),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        //Check description
        val textViewDescription = onView(
            allOf(
                withId(R.id.description),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textViewDescription.check(matches(isDisplayed()))

        //Check fact image
        val imageViewFacts = onView(
            allOf(
                withId(R.id.factImage), withContentDescription("Facts image"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        imageViewFacts.check(matches(isDisplayed()))

        //Check right arrow image
        val imageViewRightArrow = onView(
            allOf(
                withId(R.id.rightArrow), withContentDescription("Click for fact details"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        imageViewRightArrow.check(matches(isDisplayed()))
    }

    @Test
    fun testCaseForRecyclerClick() {
        onView(withId(R.id.recyclerView))
            .inRoot(
                RootMatchers.withDecorView(
                    Matchers.`is`(mActivityTestRule.activity.window.decorView)
                )
            )
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
    }

    @Test
    fun testCaseForRecyclerScroll() {
        // Get total item of RecyclerView
        val recyclerView = mActivityTestRule.activity.recyclerView
        val itemCount = recyclerView.adapter!!.itemCount

        // Scroll to end of page with position
        onView(withId(R.id.recyclerView))
            .inRoot(
                RootMatchers.withDecorView(
                    Matchers.`is`(mActivityTestRule.activity.window.decorView)
                )
            )
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemCount - 1))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.mCountingIdlingResource)
    }

}
