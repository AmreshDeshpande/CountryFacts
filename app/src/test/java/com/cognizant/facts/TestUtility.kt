package com.cognizant.facts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.cognizant.facts.data.model.Fact

class TestUtility {

    companion object {

        fun getTestFactsRepoData(): List<Fact> {
            return listOf(
                Fact(
                    "Beavers",
                    "Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony",
                    image = ("http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg")
                )
            )
        }
    }
}

class TestObserver<T> : Observer<T> {

    val observedValues = mutableListOf<T?>()

    override fun onChanged(value: T?) {
        observedValues.add(value)
        Log.d("observedValues", observedValues.toString())
    }
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}