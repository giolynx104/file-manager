package com.example.filemanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filemanager.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FileAdapter
    private var currentDirectory: File = Environment.getExternalStorageDirectory()

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
        private const val MANAGE_STORAGE_PERMISSION_REQUEST = 124
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        checkPermissions()
    }

    private fun setupRecyclerView() {
        adapter = FileAdapter { file ->
            if (file.isDirectory) {
                navigateToDirectory(file)
            } else {
                if (file.extension.lowercase() in listOf("txt", "json", "xml", "md")) {
                    openTextFile(file)
                } else {
                    Toast.makeText(this, "Only text files can be viewed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                loadFiles()
            } else {
                requestManageStoragePermission()
            }
        } else {
            if (hasPermissions()) {
                loadFiles()
            } else {
                requestPermissions()
            }
        }
    }

    private fun requestManageStoragePermission() {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
            data = Uri.parse("package:$packageName")
        }
        Toast.makeText(this, "Please grant storage permission", Toast.LENGTH_LONG).show()
        startActivityForResult(intent, MANAGE_STORAGE_PERMISSION_REQUEST)
    }

    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadFiles()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MANAGE_STORAGE_PERMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                loadFiles()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadFiles() {
        try {
            binding.currentPathText.text = currentDirectory.absolutePath
            val files = currentDirectory.listFiles()?.sortedWith(compareBy({ !it.isDirectory }, { it.name }))
            if (files == null) {
                Toast.makeText(this, "Unable to list files", Toast.LENGTH_SHORT).show()
                return
            }
            adapter.submitList(files)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDirectory(directory: File) {
        if (!directory.canRead()) {
            Toast.makeText(this, "Cannot access this directory", Toast.LENGTH_SHORT).show()
            return
        }
        currentDirectory = directory
        loadFiles()
    }

    private fun openTextFile(file: File) {
        if (!file.canRead()) {
            Toast.makeText(this, "Cannot read this file", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, FileViewerActivity::class.java).apply {
            putExtra("file_path", file.absolutePath)
        }
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (currentDirectory.absolutePath != Environment.getExternalStorageDirectory().absolutePath) {
            navigateToDirectory(currentDirectory.parentFile!!)
        } else {
            super.onBackPressed()
        }
    }
} 