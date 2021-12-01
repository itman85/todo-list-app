package com.picoder.sample.todolist.fragment.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.data.viewmodel.ToDoViewModel
import com.picoder.sample.todolist.databinding.FragmentListBinding
import com.picoder.sample.todolist.fragment.SharedViewModel
import com.picoder.sample.todolist.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_list.view.*


class ListFragment : Fragment() {

    private val todoViewModel: ToDoViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!! // access binding readonly

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        //val view = inflater.inflate(R.layout.fragment_list, container, false)
        binding.lifecycleOwner = this // this will allow binding live data in binding adapter
        binding.shareViewModel = sharedViewModel

        setupRecyclerView()

        todoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkIfDataEmpty(data)
            adapter.setData(data)
        })

        /*
        // use data binding adapter instead
        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner, {
            showEmptyDataView(it)
        })*/

        /*
        // use data binding adapter instead
        view.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }*/


        // set menu
        setHasOptionsMenu(true)

        // hide soft keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // avoid memory leak
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /*
    private fun showEmptyDataView(isEmpty: Boolean) {
        if (isEmpty) {
            view?.iv_no_data?.visibility = View.VISIBLE
            view?.tv_no_data?.visibility = View.VISIBLE
        } else {
            view?.iv_no_data?.visibility = View.INVISIBLE
            view?.tv_no_data?.visibility = View.INVISIBLE
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            confirmRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemoval() {
        var builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteAllData()
            Toast.makeText(
                requireContext(), "Successfully Removed All!",
                Toast.LENGTH_LONG
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            //
        }
        builder.setTitle("Delete All?")
        builder.setMessage("Are you sure you want to delete All?")
        builder.create().show()
    }

}