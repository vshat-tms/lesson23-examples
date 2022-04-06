package com.example.lesson23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.github.javafaker.Faker

class MainActivity : AppCompatActivity(), MainView {
    private lateinit var listView: ListView
    private val controller = MainController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.list_view)

        controller.onViewReady(this)
    }

    // обязательно в onDestroy занулять ссылку на активити
    override fun onDestroy() {
        super.onDestroy()
        controller.onViewDestroyed()
    }


    override fun displayList(users: List<User>) {
        val newAdapter = UsersListAdapter(layoutInflater, users)
        listView.adapter = newAdapter
    }

    fun addListItem(view: View)= controller.onAddRandomClicked()
    fun removeLast(view: View) = controller.onRemoveLastClicked()
    fun removeAll(view: View) = controller.onRemoveAllClicked()
    fun editSecond(view: View) = controller.onEditSecondClicked()
}