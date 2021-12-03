package com.picoder.sample.todolist.fragment.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.data.model.ToDoData
import com.picoder.sample.todolist.data.model.toPriority
import com.picoder.sample.todolist.data.viewmodel.ToDoViewModel
import com.picoder.sample.todolist.databinding.FragmentAddBinding
import com.picoder.sample.todolist.fragment.SharedViewModel
import com.picoder.sample.todolist.utils.showKeyboard


class AddFragment : Fragment() {

    private val todoViewModel: ToDoViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater)

        // set menu
        setHasOptionsMenu(true)

        binding.edtTitle.requestFocus()

        binding.edtTitle.postDelayed({ showKeyboard(requireActivity()) }, 50)

        binding.spinnerPriority.onItemSelectedListener = sharedViewModel.listener

        return binding.root
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
        val title = binding.edtTitle.text.toString()
        val priority = binding.spinnerPriority.selectedItem.toString()
        val description = binding.edtDescription.text.toString()

        val validate = sharedViewModel.validateDataFromInput(title, description)
        if (validate) {
            val newToDoData = ToDoData(0, title, priority.toPriority(), description)
            todoViewModel.insertData(newToDoData)
            Toast.makeText(requireContext(), "Successfully Added!", Toast.LENGTH_LONG).show()
            // back to list
            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        } else {
            Toast.makeText(requireContext(), "Please input data", Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}