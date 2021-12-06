package com.picoder.sample.todolist.features.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.picoder.sample.todolist.model.ToDoItem

class ToDoDiffUtil(private val oldList: List<ToDoItem>, private val newList: List<ToDoItem>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition] // compare 2 objects address
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id &&
                oldList[oldItemPosition].title == newList[newItemPosition].title &&
                oldList[oldItemPosition].description == newList[newItemPosition].description &&
                oldList[oldItemPosition].priority == newList[newItemPosition].priority
    }
}