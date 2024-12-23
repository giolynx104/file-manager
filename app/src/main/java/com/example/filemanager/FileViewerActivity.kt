package com.example.filemanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filemanager.databinding.ActivityFileViewerBinding
import java.io.File

class FileViewerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFileViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val filePath = intent.getStringExtra("file_path")
        if (filePath == null) {
            Toast.makeText(this, "Error: File path not provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val file = File(filePath)
        binding.fileNameText.text = file.name

        try {
            binding.contentText.text = file.readText()
        } catch (e: Exception) {
            Toast.makeText(this, "Error reading file: ${e.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
} 