package com.cognizant.news.data

import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


@BindingAdapter(value = ["dataState", "swipeToRefresh"], requireAll = false)
fun setStateForLoading(progressBar: ProgressBar, dataState: DataState, swipeRefreshLayout: SwipeRefreshLayout) {

    progressBar.visibility = when (dataState) {

        is DataState.Loading -> {
            if (!swipeRefreshLayout.isRefreshing) {
                View.VISIBLE
            } else
                View.GONE
        }
        is DataState.Success, is DataState.Error ->{
            View.GONE
        }
    }
}
