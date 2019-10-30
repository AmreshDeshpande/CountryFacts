package com.cognizant.facts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cognizant.facts.TestUtility.Companion.getTestFactsRepoData
import com.cognizant.facts.feature.data.api.ErrorResponse
import com.cognizant.facts.feature.data.DataState
import com.cognizant.facts.feature.FactsViewModel
import com.cognizant.facts.feature.data.model.Country
import com.cognizant.facts.feature.dataprovider.FactsRepository
import com.cognizant.facts.feature.utils.Constants
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.concurrent.CountDownLatch


class FactsViewModelTest {

    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var repository: FactsRepository

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private var liveDataUnderTest: TestObserver<DataState>? = null
    private lateinit var factsViewModel: FactsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        factsViewModel = FactsViewModel(repository)
        liveDataUnderTest = factsViewModel.getCountryDataState()?.testObserver()
    }


    @Test
    fun testViewModelLoading() {
        //Given
        val liveDataUnderTest = factsViewModel.getCountryDataState()?.testObserver()

        //When
        factsViewModel.getCountryFacts()

        //Then
        assertEquals(liveDataUnderTest?.observedValues?.size, 1)
        val dataState = liveDataUnderTest?.observedValues?.get(0) as DataState
        assertTrue(dataState == DataState.Loading)
        assertFalse(dataState is DataState.Success)
        assertFalse(dataState is DataState.Error)
    }

    @Test
    fun testViewModelSuccess() = runBlockingTest {
        val lock = CountDownLatch(1)

        //Given
        // Will be launched in the mainThreadSurrogate dispatcher
        val testFactRepoData = getTestFactsRepoData()
        whenever(repository.getFacts(any(), any())).thenAnswer {
            (it.getArgument(0) as (Country?) -> (Unit)).invoke(getTestFactsRepoData())
            lock.countDown()
        }

        //When
        factsViewModel.getCountryFacts()
        lock.await()
        assertEquals(2, liveDataUnderTest?.observedValues?.size)

        //Then
        val dataSuccess = liveDataUnderTest?.observedValues?.get(1) as DataState
        assertTrue(dataSuccess is DataState.Success)
        assertEquals((dataSuccess as DataState.Success).countryData, testFactRepoData)
        assertFalse(dataSuccess is DataState.Error)
        assertFalse(dataSuccess == DataState.Loading)

    }

    @Test
    fun testViewModelError() = runBlockingTest {
        val lock = CountDownLatch(1)
        //Given
        whenever(repository.getFacts(any(), any())).thenAnswer {
            (it.getArgument(1) as (ErrorResponse) -> (Unit)).invoke((ErrorResponse(Throwable())))
            lock.countDown()
        }

        //When
        factsViewModel.getCountryFacts()
        lock.await()

        //Then
        assertEquals(liveDataUnderTest?.observedValues?.size, 2)
        val dataError = liveDataUnderTest?.observedValues?.get(1) as DataState
        assertTrue(dataError is DataState.Error)
        assertFalse(dataError is DataState.Success)
        assertFalse(dataError == DataState.Loading)
        assertNotNull((dataError as DataState.Error).error.errorMessage)
        assertEquals(dataError.error.errorMessage, Constants.ERROR_MESSAGE)
    }


    @Test
    fun testViewModelInitialState() {
        //When
        val liveData = factsViewModel.getCountryDataState()
        val data = liveData?.value

        //Then
        assertFalse(data is DataState.Error)
        assertFalse(data is DataState.Success)
        assertFalse(data === DataState.Loading)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}

