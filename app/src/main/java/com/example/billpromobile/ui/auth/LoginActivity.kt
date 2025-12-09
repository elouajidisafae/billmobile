package com.example.billpromobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.billpromobile.data.local.preferences.AuthPreferences
import com.example.billpromobile.data.repository.AuthRepository
import com.example.billpromobile.databinding.ActivityLoginBinding
import com.example.billpromobile.ui.employe.EmployeDashboard
import com.example.billpromobile.ui.manager.ManagerDashboard
import com.example.billpromobile.ui.superadmin.SuperAdminDashboard
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authPreferences: AuthPreferences
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authPreferences = AuthPreferences(this)

        // Si déjà connecté → redirection directe
        if (authPreferences.isLoggedIn()) {
            redirectToDashboard(authPreferences.getRole()!!)
            finish()
            return
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = authRepository.login(email, password)
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!
                        authPreferences.saveUser(data.token, data.role, data.nom, data.prenom)

                        Toast.makeText(this@LoginActivity, "Bienvenue ${data.prenom} ${data.nom}", Toast.LENGTH_LONG).show()
                        redirectToDashboard(data.role)
                    } else {
                        Toast.makeText(this@LoginActivity, "Email ou mot de passe incorrect", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Erreur réseau", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun redirectToDashboard(role: String) {
        val intent = when (role) {
            "SUPERADMIN" -> Intent(this, SuperAdminDashboard::class.java)
            "MANAGER" -> Intent(this, ManagerDashboard::class.java)
            "EMPLOYE" -> Intent(this, EmployeDashboard::class.java)
            else -> return
        }
        startActivity(intent)
        finish()
    }
}