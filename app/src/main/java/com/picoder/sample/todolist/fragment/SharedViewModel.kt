package com.picoder.sample.todolist.fragment

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.data.model.ToDoData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase : MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkIfDataEmpty(toDoData: List<ToDoData>){
        emptyDatabase.value = toDoData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> (parent?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.red
                    )
                )

                1 -> (parent?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.yellow
                    )
                )

                2 -> (parent?.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.green
                    )
                )
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}

    }

    fun validateDataFromInput(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        } else !(title.isEmpty() || description.isEmpty())
    }
}