package com.picoder.sample.todolist.fragment.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.data.model.ToDoData
import com.picoder.sample.todolist.data.viewmodel.ToDoViewModel
import com.picoder.sample.todolist.databinding.FragmentListBinding
import com.picoder.sample.todolist.fragment.SharedViewModel
import com.picoder.sample.todolist.fragment.list.adapter.ListAdapter
import com.picoder.sample.todolist.utils.hideKeyboard
import com.picoder.sample.todolist.utils.observeOnce
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val todoViewModel: ToDoViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()

    private val adapter: ListAdapter by lazy { ListAdapter() }

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater)

        setupRecyclerView()

        todoViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            sharedViewModel.checkIfDataEmpty(data)
            adapter.setData(data)
            binding.recyclerView.scheduleLayoutAnimation() // call this everytime adapter update data
        })

        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner, {
            showEmptyDataView(it)
        })

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }


        // set menu
        setHasOptionsMenu(true)

        // hide soft keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        /*recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 350
        }*/
        //binding.recyclerView.scheduleLayoutAnimation()
        //
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                // delete item
                todoViewModel.deleteData(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                Toast.makeText(
                    requireContext(),
                    "Successfully Removed: '${itemToDelete.title}'",
                    Toast.LENGTH_LONG
                ).show()
                // restore item
                restoreDeletedData(viewHolder.itemView, itemToDelete)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        //snackBar.anchorView = floatingActionButton
        snackBar.setAction("Undo") {
            todoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    private fun showEmptyDataView(isEmpty: Boolean) {
        if (isEmpty) {
            binding.ivNoData.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.VISIBLE
        } else {
            binding.ivNoData.visibility = View.INVISIBLE
            binding.tvNoData.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = false
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> todoViewModel.sortByHighPriority.observe(
                this,
                { adapter.setData(it) })
            R.id.menu_priority_low -> todoViewModel.sortByLowPriority.observe(
                this,
                { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchData(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchData(query)
        }
        return true
    }

    private fun searchData(query: String) {
        todoViewModel.searchData(query).observeOnce(this, Observer {
            it?.let {
                adapter.setData(it)
                binding.recyclerView.scheduleLayoutAnimation()
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}