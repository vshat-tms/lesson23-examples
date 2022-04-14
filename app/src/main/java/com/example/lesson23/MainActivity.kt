package com.example.lesson23

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson23.databinding.ActivityMainBinding


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
                .add(R.id.fragmentContainer, ListFragment(), null)
                .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            supportActionBar?.setDisplayHomeAsUpEnabled(canNavigateUp)
        }
    }

    override fun navigateToDetailsScreen(user: User) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, DetailsFragment.newInstance(user.toString()), null)
            .addToBackStack(null)
            .commit()
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