package com.example.lesson23.screen.userdetails

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lesson23.R
import com.example.lesson23.ResultState
import com.example.lesson23.databinding.FragmentDetailsBinding
import com.example.lesson23.navigator
import com.example.lesson23.setTitle
import org.koin.core.component.KoinComponent

class UserDetailsFragment : Fragment(R.layout.fragment_details), KoinComponent {
    private lateinit var viewModel: UserDetailsViewModel

    private fun initViewModel(userId: Long) {
        // https://insert-koin.io/docs/reference/koin-android/viewmodel/
        // раздел "Passing Parameters to Constructor"
        // можно использовать by viewModel{ parametersOf(id)}

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserDetailsViewModel(
                    userId,
                    getKoin().get()
                ) as T
            }
        }

        viewModel = ViewModelProvider(this, factory).get(UserDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Подробности (база)")

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

        viewModel.currentUser.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Error -> {
                    binding.errorTextView.visibility = View.VISIBLE
                    binding.errorTextView.text = "Отсутствует пользователь"
                    binding.progressBar.visibility = View.GONE
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

        fun newInstance(userId: Long): UserDetailsFragment {
            val args = Bundle().apply {
                putLong(ARGUMENT_ID, userId)
            }

            val fragment = UserDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}