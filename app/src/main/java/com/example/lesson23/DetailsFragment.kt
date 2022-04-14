package com.example.lesson23

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lesson23.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = arguments?.getString(ARGUMENT_TEXT) ?: "Unknown"
        FragmentDetailsBinding.bind(view).apply {
            textView.text = text
        }

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Подробности"

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        private const val ARGUMENT_TEXT = "ARGUMENT_TEXT"

        fun newInstance(text: String): DetailsFragment {
            val args = Bundle().apply {
                putString(ARGUMENT_TEXT, text)
            }

            val fragment = DetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}