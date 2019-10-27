package com.cognizant.news.utils


import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(message: String){
    val snack = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    snack.show()
}
