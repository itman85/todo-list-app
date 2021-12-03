package com.picoder.sample.todolist.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocussedView = activity.currentFocus
    currentFocussedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocussedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun showKeyboard(activity: Activity) {
    val inputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocussedView = activity.currentFocus
    currentFocussedView?.let {
        inputMethodManager.showSoftInput(
            currentFocussedView, InputMethodManager.SHOW_IMPLICIT
        )
    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, myObserver: Observer<T>) {
    this.observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            myObserver.onChanged(t)
            // live data remove this observer after call onChange from myObserver
            removeObserver(Observer@ this)
        }
    })

}