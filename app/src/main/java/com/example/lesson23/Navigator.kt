package com.example.lesson23

import androidx.fragment.app.Fragment

interface Navigator {
    fun navigateToDetailsScreen(userId: Long)
    fun goBack()
}

fun Fragment.navigator(): Navigator = (requireActivity() as Navigator)