package com.picoder.sample.todolist.features.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.model.ToDoItem
import com.picoder.sample.todolist.databinding.FragmentAddBinding
import com.picoder.sample.todolist.domain.entity.toPriority
import com.picoder.sample.todolist.utils.setupListener
import com.picoder.sample.todolist.utils.showKeyboard
import com.picoder.sample.todolist.utils.validateDataFromInput
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : Fragment() {

    private val addToDoViewModel: AddToDoViewModel by viewModels()

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

        binding.spinnerPriority.setupListener(requireContext())

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

        val validate = validateDataFromInput(title, description)
        if (validate) {
            val newToDoData = ToDoItem(0, title, priority.toPriority(), description)
            addToDoViewModel.addToDo(newToDoData)
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