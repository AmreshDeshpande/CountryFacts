package com.cognizant.news.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cognizant.news.data.model.News
import com.cognizant.news.dataprovider.NewsDataRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NewsViewModel(private val newsDataRepository :NewsDataRepository) : ViewModel() {

    private val newsData: MutableLiveData<News>? = MutableLiveData()

    //Exposing live data to view and not Mutable live status
    fun getNewsData(): LiveData<News>? = newsData

    fun getNews(){
        viewModelScope.launch {
            newsDataRepository.getNews(
                success = { news ->
                    newsData?.value = news
                },
                failure = {

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