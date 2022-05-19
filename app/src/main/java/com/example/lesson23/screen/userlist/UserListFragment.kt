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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson23.R
import com.example.lesson23.databinding.FragmentListBinding
import com.example.lesson23.db.User
import com.example.lesson23.navigator
import com.example.lesson23.repository.UserSortOrder
import com.example.lesson23.setTitle
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent


class UserListFragment : Fragment(R.layout.fragment_list), KoinComponent {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: UsersListRecyclerDiffAdapter
    private val vm: UserListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        adapter = UsersListRecyclerDiffAdapter(layoutInflater,
            object : UsersListRecyclerDiffAdapter.UserClickListener {
                override fun onUserClicked(user: User) {
                    vm.onUserClicked(user)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Список (база)")

        _binding = FragmentListBinding.bind(view)

        binding.apply {
            addListItemButton.setOnClickListener {
                vm.onAddRandomClicked()
            }
            removeAllItemsButton.setOnClickListener {
                vm.onRemoveAllClicked()
            }
            removeLastItemsButton.setOnClickListener {
                vm.onRemoveLastClicked()
            }
            editSecondItemButton.setOnClickListener {
                vm.onEditSecondClicked()
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
                    vm.onSearchQueryChanged(s.toString())
                }

            })

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        vm.users.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        vm.showListEmptyMessageErrorEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        vm.openDetailsScreenEvent.observe(viewLifecycleOwner) {
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
                vm.onAddRandomClicked()
                return true
            }
            R.id.menu_item_list_delete_all -> {
                vm.onRemoveAllClicked()
                return true
            }
            R.id.menu_item_list_sort_by_name_desc -> {
                vm.setSortOrder(UserSortOrder.FIRST_NAME_DESC)
                return true
            }
            R.id.menu_item_list_sort_by_name -> {
                vm.setSortOrder(UserSortOrder.FIRST_NAME_ASC)
                return true
            }
            R.id.menu_item_list_sort_by_id -> {
                vm.setSortOrder(UserSortOrder.NONE)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}