package com.example.firebase_application

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase_application.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)

        binding.acessarBtnLogin.setOnClickListener {
            val inputEmail = binding.emailEdtLogin.text.toString().trim()
            val inputPassword = binding.passwordEdtLogin.text.toString().trim()

            if (inputEmail.isBlank() || inputPassword.isBlank()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            } else {
                loginWithEmailAndPassword(inputEmail, inputPassword)
            }
        }

        binding.cadastreseTxtLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Autenticação bem-sucedida.", Toast.LENGTH_SHORT).show()
                    sharedPreferences.edit().apply {
                        putString("email", email)
                        putString("senha", password)
                        apply()
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Falha na autenticação: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
