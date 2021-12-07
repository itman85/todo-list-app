package com.picoder.sample.todolist.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.picoder.sample.todolist.R

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

fun Spinner.setupListener(context: Context) {
    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> (parent?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red
                    )
                )

                1 -> (parent?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.yellow
                    )
                )

                2 -> (parent?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.green
                    )
                )
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}

    }
    this.onItemSelectedListener = listener
}


fun validateDataFromInput(title: String, description: String): Boolean {
    return !(title.isEmpty() || description.isEmpty())
}