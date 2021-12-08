package com.picoder.sample.todolist.features.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.picoder.sample.todolist.R
import com.picoder.sample.todolist.databinding.FragmentListBinding
import com.picoder.sample.todolist.domain.entity.Priority
import com.picoder.sample.todolist.features.list.adapter.ListAdapter
import com.picoder.sample.todolist.features.list.redux.ListToDoAction
import com.picoder.sample.todolist.features.list.redux.ListToDoNavigation
import com.picoder.sample.todolist.model.ToDoItem
import com.picoder.sample.todolist.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

// need entry point to create ListToDoViewModel(HiltViewModel)
@AndroidEntryPoint
class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel: ListToDoViewModel by viewModels()

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

        binding.floatingActionButton.setOnClickListener {
            viewModel.doAction(ListToDoAction.AddNewToDoItem)
        }

        // set menu
        setHasOptionsMenu(true)

        // hide soft keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, { state ->
            adapter.setData(state.items)
            showEmptyDataView(state.isEmpty())
        })

        viewModel.nav.observe(viewLifecycleOwner, { nav ->
            when (nav) {
                is ListToDoNavigation.ShowDeleteItemSuccessMessageAndRestoreSnackbar -> {
                    /*Toast.makeText(
                        requireContext(),
                        nav.message,
                        Toast.LENGTH_LONG
                    ).show()*/
                    restoreDeletedData(nav.message)
                }
                is ListToDoNavigation.ShowDeleteAllSuccessMessage -> Toast.makeText(
                    requireContext(),
                    nav.message,
                    Toast.LENGTH_LONG
                ).show()

                is ListToDoNavigation.OpenAddNewToDoItem -> findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
        })

        viewModel.doAction(ListToDoAction.OpenScreen)
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                viewModel.doAction(ListToDoAction.SwipeToDeleteToDoItem(itemToDelete,viewHolder.adapterPosition))
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(message:String) {
        val snackBar = Snackbar.make(
            binding.root, message,
            Snackbar.LENGTH_LONG
        )
        //snackBar.anchorView = floatingActionButton
        snackBar.setAction("Undo") {
            viewModel.doAction(ListToDoAction.RestoreDeletedItem)
        }
        snackBar.behavior = object : BaseTransientBottomBar.Behavior() {
            override fun canSwipeDismissView(child: View): Boolean {
                return false // only dismiss when timeout or user tap Undo
            }
        }
        snackBar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                // only handle for case of snackbar dismiss by timeout
                if(event == DISMISS_EVENT_TIMEOUT) {
                    viewModel.doAction(ListToDoAction.ClearRestoredData)
                }
            }
        })
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
            R.id.menu_priority_high -> viewModel.doAction(ListToDoAction.SortToDoList(Priority.HIGH))
            R.id.menu_priority_low -> viewModel.doAction(ListToDoAction.SortToDoList(Priority.LOW))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.doAction((ListToDoAction.DeleteAll))
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
        viewModel.doAction(ListToDoAction.SearchToDoList(query))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}