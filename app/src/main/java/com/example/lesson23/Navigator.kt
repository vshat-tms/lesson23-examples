package com.example.lesson23

import androidx.fragment.app.Fragment

interface Navigator {
    fun navigateToDetailsScreen(user: User)
}

fun Fragment.navigator(): Navigator = (requireActivity() as Navigator)