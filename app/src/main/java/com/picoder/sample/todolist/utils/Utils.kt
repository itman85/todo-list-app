package com.picoder.sample.todolist.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(activity: Activity){
    val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocussedView = activity.currentFocus
    currentFocussedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocussedView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}