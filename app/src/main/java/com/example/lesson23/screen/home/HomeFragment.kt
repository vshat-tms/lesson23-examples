package com.example.lesson23.screen.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.lesson23.R
import com.example.lesson23.databinding.FragmentHomeBinding
import com.example.lesson23.navigator
import com.example.lesson23.setTitle

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Меню")

        FragmentHomeBinding.bind(view).apply {
            goToDb.setOnClickListener {
                navigator().navigateToDbListScreen()
            }

            goToNetwork.setOnClickListener {
                navigator().navigateToNetworkListScreen()
            }
        }
    }
}