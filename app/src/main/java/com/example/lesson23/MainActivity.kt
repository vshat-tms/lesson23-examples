package com.example.lesson23

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lesson23.databinding.ActivityMainBinding
import com.example.lesson23.screen.home.HomeFragment
import com.example.lesson23.screen.networkuserdetails.NetworkUserDetailsFragment
import com.example.lesson23.screen.networkuserlist.NetworkUserListFragment
import com.example.lesson23.screen.userdetails.UserDetailsFragment
import com.example.lesson23.screen.userlist.UserListFragment


class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: ActivityMainBinding

    private val canNavigateUp: Boolean
        get() = supportFragmentManager.backStackEntryCount > 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, HomeFragment(), null)
                .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            updateBackButton()
        }

        updateBackButton()
    }

    override fun navigateToDbListScreen() =
        goTo { UserListFragment() }

    override fun navigateToNetworkListScreen() =
        goTo { NetworkUserListFragment() }

    override fun navigateToDetailsScreen(userId: Long) =
        goTo { UserDetailsFragment.newInstance(userId) }

    override fun navigateToNetworkDetailsScreen(userId: Long) =
        goTo { NetworkUserDetailsFragment.newInstance(userId) }

    private fun goTo(fragmentFactory: () -> Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragmentFactory(), null)
            .addToBackStack(null)
            .commit()
    }

    override fun goBack() {
        supportFragmentManager.popBackStack()
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (canNavigateUp) {
            goBack()
            false
        } else {
            finish()
            true
        }
    }

    private fun updateBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(canNavigateUp)
    }
}