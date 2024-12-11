package com.example.firebase_application

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Display.Mode
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_splash_screen)
        auth = FirebaseAuth.getInstance()

        var Timer: Int
        if (firstOpen()) Timer = 6000 else Timer = 3000
        Handler().postDelayed({
            val savedEmail = sharedPreferences.getString("email", null)
            val savedPassword = sharedPreferences.getString("senha", null)

            if (!savedEmail.isNullOrBlank() && !savedPassword.isNullOrBlank()) {
                loginWithEmailAndPassword(savedEmail, savedPassword)
            } else {
                val newActivity = Intent(this, LoginActivity::class.java)
                startActivity(newActivity)
                finish()
            }
        }, Timer.toLong())
    }

    private fun firstOpen (): Boolean {
        sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val firstOpen = sharedPreferences.getBoolean("FIRST_OPEN", true)
        if (firstOpen) {
            sharedPreferences.edit().putBoolean("FIRST_OPEN", false).apply()
            return true
        }
        return false
    }

    private fun loginWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Salva as credenciais no SharedPreferences
                    sharedPreferences.edit().apply {
                        putString("email", email)
                        putString("senha", password)
                        apply()
                    }

                    // Navega para a MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Falha na autenticação: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}