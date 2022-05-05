package com.example.lesson23.screen.userlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson23.AppExecutors
import com.example.lesson23.R
import com.example.lesson23.databinding.FragmentListBinding
import com.example.lesson23.db.AppDatabase
import com.example.lesson23.db.User
import com.example.lesson23.navigator
import com.example.lesson23.repository.UserRepository
import com.example.lesson23.repository.UserSortOrder
import com.example.lesson23.setTitle
import com.github.javafaker.Faker

class UserListFragment : Fragment(R.layout.fragment_list) {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: UsersListRecyclerDiffAdapter
    private lateinit var viewModel: UserListViewModel

    private fun initViewModel() {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserListViewModel(
                    UserRepository(
                        AppDatabase.instance.userDao(),
                        AppExecutors.ioExecutor,
                        Faker.instance()
                    )
                ) as T
            }
        }

        viewModel = ViewModelProvider(this, factory).get(UserListViewModel::class.java)
    }

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
        setTitle("Список (база)")

        initViewModel()

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
            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable) {
                    viewModel.onSearchQueryChanged(s.toString())
                }

            })

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

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
            R.id.menu_item_list_sort_by_name_desc -> {
                viewModel.setSortOrder(UserSortOrder.FIRST_NAME_DESC)
                return true
            }
            R.id.menu_item_list_sort_by_name -> {
                viewModel.setSortOrder(UserSortOrder.FIRST_NAME_ASC)
                return true
            }
            R.id.menu_item_list_sort_by_id -> {
                viewModel.setSortOrder(UserSortOrder.NONE)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}