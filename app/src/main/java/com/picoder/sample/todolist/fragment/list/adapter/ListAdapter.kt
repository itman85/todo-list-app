package com.picoder.sample.todolist.fragment.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.data.model.Priority
import com.picoder.sample.todolist.data.model.ToDoData
import com.picoder.sample.todolist.databinding.TodoItemLayoutBinding
import com.picoder.sample.todolist.fragment.list.ListFragmentDirections


class ListAdapter : RecyclerView.Adapter<ListAdapter.ToDoViewHolder>() {

    // 1. create item layout
    // 2. create view holder
    // 3. create adapter

    var dataList = emptyList<ToDoData>()

    class ToDoViewHolder(private val binding: TodoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData: ToDoData) {
            binding.tvTitle.text = toDoData.title
            binding.tvDescription.text = toDoData.description
            binding.itemBg.setOnClickListener {
                val actionDirection =
                    ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem = toDoData)
                binding.root.findNavController().navigate(actionDirection)
            }
            when (toDoData.priority) {
                Priority.HIGH -> binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.red
                    )
                )
                Priority.MEDIUM -> binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.yellow
                    )
                )
                Priority.LOW -> binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.green
                    )
                )
            }
        }

        companion object {
            fun from(parent: ViewGroup): ToDoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TodoItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ToDoViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(dataList, toDoData)
        val diffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
        diffResult.dispatchUpdatesTo(this)
    }
}