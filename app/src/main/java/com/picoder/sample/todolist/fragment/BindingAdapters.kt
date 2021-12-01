package com.picoder.sample.todolist.fragment

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.data.model.Priority
import com.picoder.sample.todolist.data.model.ToDoData
import com.picoder.sample.todolist.data.model.toIndex
import com.picoder.sample.todolist.fragment.list.ListFragmentDirections

// for custom data binding
class BindingAdapters {

    companion object {

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:emptyData")
        @JvmStatic
        fun emptyData(view: View, emptyData: MutableLiveData<Boolean>) {
            when (emptyData.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:priorityToIndex")
        @JvmStatic
        fun priorityToIndex(spinner: Spinner, priority: Priority) {
            spinner.setSelection(priority.toIndex())
        }

        @BindingAdapter("android:priorityToColor")
        @JvmStatic
        fun priorityToColor(view: CardView, priority: Priority) {
            when (priority) {
                Priority.HIGH -> view.setCardBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.red
                    )
                )
                Priority.MEDIUM -> view.setCardBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.yellow
                    )
                )
                Priority.LOW -> view.setCardBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.green
                    )
                )
            }
        }

        @BindingAdapter("sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout, currentItem: ToDoData) {
            view.setOnClickListener {
                val actionDirection = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(actionDirection)
            }
        }
    }
}