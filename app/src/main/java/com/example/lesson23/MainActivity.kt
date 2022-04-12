package com.example.lesson23

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File

private const val REQUEST_PERMISSION_CODE = 2

class MainActivity : AppCompatActivity(), MainView {
    private lateinit var listView: ListView
    private val controller = MainController()
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.list_view)

        controller.onViewReady(this)

        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
    }

    override fun askForWriteExternalStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        );
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

    val CREATE_FILE = 1

    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "invoice.pdf")

        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CREATE_FILE)
        } else {
            Toast.makeText(this, "can't open 'Files' app", Toast.LENGTH_SHORT).show()
        }


    }

    fun example1() {
        var counterValue = 0
        if (sharedPreferences.contains("counter")) {
            counterValue = sharedPreferences.getInt("counter", 0)
            Log.d("MainActivityExample", "counter value = $counterValue")
        } else {
            Log.d("MainActivityExample", "No counter value")
        }

        val newCounterValue = counterValue + 1
        sharedPreferences
            .edit()
            .putInt("counter", newCounterValue)
            .putString("Greetings", "Hello, World!")
            .apply()


        File(filesDir, "example.txt").apply {
            appendText("hello\n")
        }

        File(cacheDir, "cache-example.txt").apply {
            appendText("cache")
        }

        val downloadsFolder = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )

        val ourFolder = File(downloadsFolder, "notes-app")
        ourFolder.mkdir()

        File(ourFolder, "downloads-example.txt").apply {
            appendText("downloads example")
        }
    }

    fun addListItem(view: View) = controller.onAddRandomClicked()
    fun removeLast(view: View) = controller.onRemoveLastClicked()
    fun removeAll(view: View) = controller.onRemoveAllClicked()
    fun editSecond(view: View) = controller.onEditSecondClicked()
}