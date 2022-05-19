package com.example.lesson23

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

class PermissionChecker(private val applicationContext: Context) {

    fun hasWriteExternalStoragePermission() =
        applicationContext.checkSelfPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
}


