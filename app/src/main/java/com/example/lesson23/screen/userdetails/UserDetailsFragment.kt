package com.example.lesson23.screen.userdetails

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lesson23.AppExecutors
import com.example.lesson23.R
import com.example.lesson23.databinding.FragmentDetailsBinding
import com.example.lesson23.db.AppDatabase
import com.example.lesson23.navigator
import com.example.lesson23.repository.UserRepository
import com.github.javafaker.Faker

class UserDetailsFragment : Fragment(R.layout.fragment_details) {
    private lateinit var viewModel: UserDetailsViewModel

    private fun initViewModel(userId: Long) {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return UserDetailsViewModel(
                    userId,
                    UserRepository(
                        AppDatabase.instance.userDao(),
                        AppExecutors.ioExecutor,
                        Faker.instance()
                    )
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
            binding.firstNameEditText.setText(it?.firstName ?: "")
            binding.lastNameEditText.setText(it?.lastName ?: "")
        }

        viewModel.isUserMissingError.observe(viewLifecycleOwner) {
            binding.errorTextView.visibility = if(it) View.VISIBLE else View.GONE
            binding.errorTextView.text = "Отсутствует пользователь"
        }

        viewModel.needCloseScreen.observe(viewLifecycleOwner) {
            if(it) {
                navigator().goBack()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
            binding.infoView.visibility = if(!it) View.VISIBLE else View.GONE
        }

        viewModel.isSavingUser.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if(it) View.VISIBLE else View.GONE
            binding.infoView.visibility = if(!it) View.VISIBLE else View.GONE
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Подробности"
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