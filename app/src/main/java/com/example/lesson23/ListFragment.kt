package com.example.lesson23

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.lesson23.databinding.FragmentListBinding

private const val REQUEST_PERMISSION_CODE = 2


class ListFragment : Fragment(R.layout.fragment_list),
    com.example.lesson23.ListView {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!


    private val controller = ListController()
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireContext().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListBinding.bind(view)

        binding.apply {
            addListItemButton.setOnClickListener {
                controller.onAddRandomClicked()
            }
            removeAllItemsButton.setOnClickListener {
                controller.onRemoveAllClicked()
            }
            removeLastItemsButton.setOnClickListener {
                controller.onRemoveLastClicked()
            }
            editSecondItemButton.setOnClickListener {
                controller.onEditSecondClicked()
            }

            listView.setOnItemClickListener { parent, view, position, id ->
                val user = parent.getItemAtPosition(position) as User
                navigator().navigateToDetailsScreen(user)
            }
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Список"


        controller.onViewReady(this)
    }

    override fun onDestroyView() {
        controller.onViewDestroyed()
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_list_add_item -> {
                controller.onAddRandomClicked()
                return true
            }
            R.id.menu_item_list_delete_all -> {
                controller.onRemoveAllClicked()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun displayList(users: List<User>) {
        val newAdapter = UsersListAdapter(layoutInflater, users)
        binding.listView.adapter = newAdapter
    }

    override fun askForWriteExternalStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        );
    }

}