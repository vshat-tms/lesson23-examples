package com.example.lesson23

import androidx.fragment.app.Fragment

interface Navigator {
    fun navigateToDbListScreen()
    fun navigateToNetworkListScreen()
    fun navigateToDetailsScreen(userId: Long)
    fun navigateToNetworkDetailsScreen(userId: Long)
    fun goBack()
}

fun Fragment.navigator(): Navigator = (requireActivity() as Navigator)