package com.picoder.sample.todolist.features.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.databinding.FragmentAddBinding
import com.picoder.sample.todolist.domain.entity.toPriority
import com.picoder.sample.todolist.features.add.redux.AddToDoAction
import com.picoder.sample.todolist.features.add.redux.AddToDoNavigation
import com.picoder.sample.todolist.utils.setupListener
import com.picoder.sample.todolist.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : Fragment() {

    private val viewModel: AddToDoViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater)

        // set menu
        setHasOptionsMenu(true)

        binding.edtTitle.requestFocus()

        binding.edtTitle.postDelayed({ showKeyboard(requireActivity()) }, 50)

        binding.spinnerPriority.setupListener(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, Observer {
            // handle update UI as state changed
        })

        viewModel.nav.observe(viewLifecycleOwner, Observer { navigation ->
            when (navigation) {
                is AddToDoNavigation.ShowMessageAndBackToList -> {
                    Toast.makeText(requireContext(), navigation.message, Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_addFragment_to_listFragment)
                }

                is AddToDoNavigation.ShowErrorMessage -> Toast.makeText(
                    requireContext(),
                    navigation.message,
                    Toast.LENGTH_LONG
                ).show()

                else -> {
                    // do nothing
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            saveData()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveData() {
        val title = binding.edtTitle.text.toString()
        val priority = binding.spinnerPriority.selectedItem.toString()
        val description = binding.edtDescription.text.toString()
        viewModel.dispatchAction(
            AddToDoAction.AddToDoItem(
                title,
                priority.toPriority(),
                description
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}