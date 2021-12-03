package com.picoder.sample.todolist.fragment.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.data.model.ToDoData
import com.picoder.sample.todolist.data.model.toIndex
import com.picoder.sample.todolist.data.model.toPriority
import com.picoder.sample.todolist.data.viewmodel.ToDoViewModel
import com.picoder.sample.todolist.databinding.FragmentUpdateBinding
import com.picoder.sample.todolist.fragment.SharedViewModel
import com.picoder.sample.todolist.utils.showKeyboard


class UpdateFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by viewModels()

    private val todoViewModel: ToDoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null

    private val binding get() = _binding!!

    // UpdateFragmentArgs is generated by safeargs
    private val args by navArgs<UpdateFragmentArgs>() // get argument data from navigation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater)

        setHasOptionsMenu(true)

        binding.edtCurrentTitle.setText(args.currentItem.title)
        binding.edtCurrentDescription.setText(args.currentItem.description)
        binding.spinnerCurrentPriority.setSelection(args.currentItem.priority.toIndex())
        binding.spinnerCurrentPriority.onItemSelectedListener = sharedViewModel.listener

        binding.edtCurrentTitle.requestFocus()

        binding.edtCurrentTitle.postDelayed({ showKeyboard(requireActivity()) }, 50)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save) {
            updateItem()
        } else if (item.itemId == R.id.menu_delete) {
            confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmItemRemoval() {
        var builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteData(args.currentItem)
            Toast.makeText(
                requireContext(), "Successfully Removed: ${args.currentItem.title}",
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ ->
            //
        }
        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure you want to delete '${args.currentItem.title}'?")
        builder.create().show()
    }

    private fun updateItem() {
        val title = binding.edtCurrentTitle.text.toString()
        val description = binding.edtCurrentDescription.text.toString()
        val priority = binding.spinnerCurrentPriority.selectedItem.toString()

        val validation = sharedViewModel.validateDataFromInput(title, description)
        if (validation) {
            val updatedItem = ToDoData(
                args.currentItem.id,
                title,
                priority.toPriority(),
                description
            )
            todoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please input all fields!", Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}