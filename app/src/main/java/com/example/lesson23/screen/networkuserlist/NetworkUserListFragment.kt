package com.example.lesson23.screen.networkuserlist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson23.R
import com.example.lesson23.ResultState
import com.example.lesson23.databinding.FragmentNetworkListBinding
import com.example.lesson23.db.User
import com.example.lesson23.di.DependencyStorage
import com.example.lesson23.navigator
import com.example.lesson23.network.InvalidResponseException
import com.example.lesson23.network.UserNotFoundException
import com.example.lesson23.screen.userlist.UsersListRecyclerDiffAdapter
import com.example.lesson23.setTitle

class NetworkUserListFragment : Fragment(R.layout.fragment_network_list) {
    private lateinit var viewModel: NetworkUserListViewModel
    private lateinit var adapter: UsersListRecyclerDiffAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = UsersListRecyclerDiffAdapter(layoutInflater,
            object : UsersListRecyclerDiffAdapter.UserClickListener {
                override fun onUserClicked(user: User) {
                    viewModel.onUserClicked(user)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Список (сеть)")

        val binding = FragmentNetworkListBinding.bind(view).apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        initViewModel()
        viewModel.users.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Error -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.statusTextView.text = when (it.throwable) {
                        is UserNotFoundException -> "Ошибка: пользователь не найден"
                        is InvalidResponseException -> "Ошибка: некорректный ответ сервера"
                        else -> "Ошибка запроса"
                    }
                    Log.e("NetworkUserListFragment", it.throwable.stackTraceToString())
                }
                is ResultState.Loading -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.statusTextView.text = "Загрузка..."
                }
                is ResultState.Success -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.statusTextView.text = "Пользователей: " + it.data.size
                    adapter.setData(it.data)
                }
            }
        }

        viewModel.openDetailsScreenEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { id ->
                navigator().navigateToNetworkDetailsScreen(id)
            }
        }
    }

    private fun initViewModel() {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NetworkUserListViewModel(
                    DependencyStorage.Repositories.networkUserRepository,
                    DependencyStorage.Mappers.reqresUserMapper
                ) as T
            }
        }

        viewModel = ViewModelProvider(this, factory).get(NetworkUserListViewModel::class.java)
    }

}