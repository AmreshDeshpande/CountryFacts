package com.cognizant.facts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cognizant.facts.data.api.ErrorResponse
import com.cognizant.facts.data.api.FactsApiService
import com.cognizant.facts.data.model.Country
import com.cognizant.facts.data.model.Fact
import com.cognizant.facts.dataprovider.FactsRepositoryImpl
import com.cognizant.facts.dataprovider.FactsRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Response

class FactsRepositoryTest {

    @Rule
    @JvmField
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var factsApiService: FactsApiService

    @Mock
    lateinit var response: Response<List<Fact>?>

    private lateinit var repository: FactsRepository

    @Mock
    lateinit var success: (Country?) -> (Unit)

    @Mock
    lateinit var error: (ErrorResponse) -> (Unit)

    @Before
    fun setUp() {
        //Given
        repository = FactsRepositoryImpl(factsApiService)
    }

    @Test
    fun testDataRepoSuccess() = runBlocking {
        //Given
        whenever(response.body()).thenAnswer { TestUtility.getTestFactsRepoData() }
        whenever(response.isSuccessful).thenAnswer { true }
        whenever(factsApiService.getFacts()).thenAnswer {
            response
        }
        //When
        repository.getFacts(success, error)
        //Then
        verify(success)(any())
    }


    @Test
    fun testDataRepoFailure() = runBlocking {
        //Given
        whenever(response.body()).thenAnswer { TestUtility.getTestFactsRepoData() }
        whenever(response.isSuccessful).thenAnswer { false }
        whenever(factsApiService.getFacts()).thenAnswer {
            response
        }
        //When
        repository.getFacts(success, error)
        //Then
        verify(error)(any())
    }
}
