package com.picoder.sample.todolist.fragment.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.data.model.ToDoData
import com.picoder.sample.todolist.data.model.toPriority
import com.picoder.sample.todolist.data.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.fragment_add.*


class AddFragment : Fragment() {

    private val todoViewModel:ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val title = edtTitle.text.toString()
        val priority = spinnerPriority.selectedItem.toString()
        val description = edtDescription.text.toString()

        val validate = validateDataFromInput(title, description)
        if (validate) {
            val newToDoData = ToDoData(0, title, priority.toPriority(), description)
            todoViewModel.insertData(newToDoData)
            Toast.makeText(requireContext(),"Successfully Added!",Toast.LENGTH_LONG).show()
            // back to list
            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        } else {
            Toast.makeText(requireContext(),"Please input data",Toast.LENGTH_LONG).show()
        }

    }

    private fun validateDataFromInput(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        } else !(title.isEmpty() || description.isEmpty())
    }
}