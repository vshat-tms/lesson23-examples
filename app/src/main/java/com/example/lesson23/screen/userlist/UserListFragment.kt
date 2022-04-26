package com.example.lesson23.screen.userlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson23.R
import com.example.lesson23.User
import com.example.lesson23.databinding.FragmentListBinding
import com.example.lesson23.navigator

class UserListFragment : Fragment(R.layout.fragment_list){
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: UsersListRecyclerDiffAdapter
    private lateinit var viewModel: UserListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        adapter = UsersListRecyclerDiffAdapter(layoutInflater,
            object : UsersListRecyclerDiffAdapter.UserClickListener {
                override fun onUserClicked(user: User) {
                    viewModel.onUserClicked(user)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)

        _binding = FragmentListBinding.bind(view)

        binding.apply {
            addListItemButton.setOnClickListener {
                viewModel.onAddRandomClicked()
            }
            removeAllItemsButton.setOnClickListener {
                viewModel.onRemoveAllClicked()
            }
            removeLastItemsButton.setOnClickListener {
                viewModel.onRemoveLastClicked()
            }
            editSecondItemButton.setOnClickListener {
                viewModel.onEditSecondClicked()
            }

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Список"

        viewModel.users.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.showListEmptyMessageErrorEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.openDetailsScreenEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { id ->
                navigator().navigateToDetailsScreen(id)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_list_add_item -> {
                viewModel.onAddRandomClicked()
                return true
            }
            R.id.menu_item_list_delete_all -> {
                viewModel.onRemoveAllClicked()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}