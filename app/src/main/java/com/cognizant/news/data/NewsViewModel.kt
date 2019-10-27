package com.cognizant.news.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognizant.news.dataprovider.NewsDataRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NewsViewModel(private val newsDataRepository :NewsDataRepository) : ViewModel() {

    private val newsDataState: MutableLiveData<DataState>? = MutableLiveData()

    //Exposing live data to view and not Mutable live status
    fun getNewsData(): LiveData<DataState>? = newsDataState

    fun getNews(){
        newsDataState?.value = DataState.Loading
        viewModelScope.launch {
            newsDataRepository.getNews(
                success = { news ->
                    newsDataState?.value = DataState.Success(news)
                },
                failure = { error ->
                    newsDataState?.value = DataState.Error(error)
                }
            )
        }
    }
}


class NewsViewModelFactory(private val newsDataRepository :NewsDataRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            NewsViewModel(this.newsDataRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}