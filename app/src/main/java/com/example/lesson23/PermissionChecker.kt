package com.example.lesson23

import android.Manifest
import android.content.pm.PackageManager

object PermissionChecker {
    fun hasWriteExternalStoragePermission() =
        MyApplication.instance.checkSelfPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
}


