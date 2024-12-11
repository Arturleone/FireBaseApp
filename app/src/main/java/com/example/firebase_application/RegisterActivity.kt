package com.example.firebase_application

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase_application.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cadastrarBtnRegister.setOnClickListener {
            val inputNome = binding.nomeEdtRegister.text.toString()
            val inputEmail = binding.emailEdtRegister.text.toString()
            val inputPassword = binding.passwordEdtRegister.text.toString()
            val inputConfirmPassword = binding.confirmpasswordEdtRegister.text.toString()

            if (inputNome.isBlank() || inputEmail.isBlank() || inputPassword.isBlank() || inputConfirmPassword.isBlank()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (inputPassword != inputConfirmPassword) {
                Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Erro ao registrar: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    fun backLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
