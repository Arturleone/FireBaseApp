package com.example.firebase_application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase_application.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: List<Task>,
    private val onDelete: (Task) -> Unit,
    private val onUpdate: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.textViewTitle.text = task.title
            binding.textViewDescription.text = task.description
            binding.checkBoxCompleted.isChecked = task.completed

            binding.checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
                task.completed = isChecked
                onUpdate(task)
            }

            binding.buttonDeleteTask.setOnClickListener {
                onDelete(task)
            }
        }
    }
}
