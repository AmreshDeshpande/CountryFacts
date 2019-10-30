package com.cognizant.facts.ui

import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cognizant.facts.data.DataState

@BindingAdapter(value = ["dataState", "swipeToRefresh"], requireAll = false)
fun setStateForLoading(
    progressBar: ProgressBar,
    dataState: DataState,
    swipeRefreshLayout: SwipeRefreshLayout
) {
    progressBar.visibility = when (dataState) {

        is DataState.Loading -> {
            if (!swipeRefreshLayout.isRefreshing) {
                View.VISIBLE
            } else
                View.GONE
        }
        is DataState.Success, is DataState.Error -> {
            View.GONE
        }
    }
}
