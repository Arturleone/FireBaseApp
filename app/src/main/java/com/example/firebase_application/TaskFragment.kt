package com.example.firebase_application

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase_application.databinding.FragmentTaskBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val taskList = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    companion object {
        private const val TAG = "TaskFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskAdapter = TaskAdapter(taskList,
            onDelete = { deleteTask(it) },
            onUpdate = { updateTask(it) }
        )

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.adapter = taskAdapter

        binding.buttonSaveTask.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                binding.editTextDescription.text.clear()
                binding.editTextTitle.text.clear()
                saveTask(title, description)
            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }

        loadTasks()
    }

    private fun saveTask(title: String, description: String) {
        val user = auth.currentUser
        if (user != null) {
            val task = Task(title, description, user.uid)
            firestore.collection("tasks")
                .add(task)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Tarefa criada com sucesso!", Toast.LENGTH_SHORT).show()
                    loadTasks()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Erro ao criar tarefa: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadTasks() {
        val user = auth.currentUser
        if (user != null) {
            firestore.collection("tasks")
                .whereEqualTo("userId", user.uid)
                .get()
                .addOnSuccessListener { documents ->
                    taskList.clear()
                    for (document in documents) {
                        val task = document.toObject(Task::class.java).copy(id = document.id)
                        taskList.add(task)
                    }
                    taskAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Erro ao carregar tarefas: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateTask(task: Task) {
        firestore.collection("tasks").document(task.id!!)
            .set(task)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Tarefa atualizada!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Erro ao atualizar tarefa: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteTask(task: Task) {
        firestore.collection("tasks").document(task.id!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Tarefa excluída!", Toast.LENGTH_SHORT).show()
                taskList.remove(task)
                taskAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Erro ao excluir tarefa: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
