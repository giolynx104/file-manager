package com.example.filemanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filemanager.databinding.ItemFileBinding
import java.io.File

class FileAdapter(private val onItemClick: (File) -> Unit) :
    ListAdapter<File, FileAdapter.FileViewHolder>(FileDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = ItemFileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FileViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FileViewHolder(
        private val binding: ItemFileBinding,
        private val onItemClick: (File) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(file: File) {
            binding.nameText.text = file.name
            binding.iconView.setImageResource(
                if (file.isDirectory) android.R.drawable.ic_menu_more
                else android.R.drawable.ic_menu_edit
            )
            binding.root.setOnClickListener { onItemClick(file) }
        }
    }

    class FileDiffCallback : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.absolutePath == newItem.absolutePath
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.absolutePath == newItem.absolutePath &&
                    oldItem.lastModified() == newItem.lastModified()
        }
    }
} 