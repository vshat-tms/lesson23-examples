package com.example.lesson23.screen.networkuserdetails

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lesson23.R
import com.example.lesson23.ResultState
import com.example.lesson23.databinding.FragmentDetailsBinding
import com.example.lesson23.di.DependencyStorage
import com.example.lesson23.navigator
import com.example.lesson23.network.InvalidResponseException
import com.example.lesson23.network.UserNotFoundException
import com.example.lesson23.setTitle

class NetworkUserDetailsFragment : Fragment(R.layout.fragment_details) {
    private lateinit var viewModel: NetworkUserDetailsViewModel

    private fun initViewModel(userId: Long) {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NetworkUserDetailsViewModel(
                    userId,
                    DependencyStorage.Repositories.networkUserRepository,
                    DependencyStorage.Mappers.reqresUserMapper
                ) as T
            }
        }

        viewModel = ViewModelProvider(this, factory).get(NetworkUserDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Подробности (сеть)")

        val userId = arguments?.getLong(ARGUMENT_ID) ?: INVALID_ID
        initViewModel(userId)

        val binding = FragmentDetailsBinding.bind(view).apply {
            saveButton.setOnClickListener {
                viewModel.editUser(
                    firstNameEditText.text.toString(),
                    lastNameEditText.text.toString()
                )
            }
        }

        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Error -> {
                    binding.infoView.visibility = View.VISIBLE
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.errorTextView.text = when (it.throwable) {
                        is UserNotFoundException -> "Ошибка: пользователь не найден"
                        is InvalidResponseException -> "Ошибка: некорректный ответ сервера"
                        else -> "Ошибка запроса"
                    }
                    binding.progressBar.visibility = View.GONE
                    Log.e("NetworkUserDetailsFragment", it.throwable.stackTraceToString())
                }
                is ResultState.Loading -> {
                    binding.errorTextView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.infoView.visibility = View.GONE
                }
                is ResultState.Success -> {
                    binding.errorTextView.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.infoView.visibility = View.VISIBLE

                    binding.firstNameEditText.setText(it.data.firstName)
                    binding.lastNameEditText.setText(it.data.lastName)
                }
            }
        }

        viewModel.needCloseScreen.observe(viewLifecycleOwner) {
            if (it) {
                navigator().goBack()
            }
        }

        viewModel.isSavingUser.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            binding.infoView.visibility = if (!it) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        private const val ARGUMENT_ID = "ARGUMENT_ID"
        private const val INVALID_ID = -1L

        fun newInstance(userId: Long): NetworkUserDetailsFragment {
            val args = Bundle().apply {
                putLong(ARGUMENT_ID, userId)
            }

            val fragment = NetworkUserDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}