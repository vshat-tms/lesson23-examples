package com.example.lesson23

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson23.databinding.ActivityMainBinding
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
                .add(R.id.fragmentContainer, UserListFragment(), null)
                .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            supportActionBar?.setDisplayHomeAsUpEnabled(canNavigateUp)
        }
    }

    override fun navigateToDetailsScreen(userId: Long) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, UserDetailsFragment.newInstance(userId), null)
            .addToBackStack(null)
            .commit()
    }

    override fun goBack() {
        supportFragmentManager.popBackStack()
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (canNavigateUp) {
            supportFragmentManager.popBackStack()
            false
        } else {
            finish()
            true
        }
    }


}