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
import io.reactivex.rxjava3.core.Observable
import java.util.*
import java.util.concurrent.TimeUnit

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

/**
 * Using Rx's throttling to improve navigation UX: clicking multiple times quickly should navigate only once without any delay
 * (using "debounce" introducing delay)
 *
 * @param A: input stream of any value of [A] (mostly Redux Action)
 * @param N: output navigation provided by [navigationTransformer]
 */
fun <A : Any, N : Any> Observable<A>.navigationWithThrottling(
    throttleMilliseconds: Long,
    navigationTransformer: (A) -> N?
): Observable<N> =
    this
        .map { action ->
            Optional.ofNullable(navigationTransformer(action))
        }
        // important to filter before throttling, so Optional.empty doesn't count into throttling,
        // otherwise we would miss navigation of the previous action product empty nav.
        .filter { it.isPresent }
        .throttleFirst(throttleMilliseconds, TimeUnit.MILLISECONDS)
        .map { it.get() }

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