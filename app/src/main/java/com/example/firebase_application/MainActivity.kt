package com.example.firebase_application

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.firebase_application.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(TaskFragment())

        binding.bottomNavMenu.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.tarefa_bottom_menu -> replaceFragment(TaskFragment())
                R.id.diario_bottom_menu -> replaceFragment(DiarioFragment())
                else -> {
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser
        if (usuarioAtual == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun logoutAccount(view: View) {
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this)
        alertDialog.setTitle("Confirmar Logout")
        alertDialog.setMessage("Tem certeza de que deseja sair da conta?")
        alertDialog.setPositiveButton("Sim") { _, _ ->
            FirebaseAuth.getInstance().signOut()

            val sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        alertDialog.setNegativeButton("Não") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.create().show()
    }

}