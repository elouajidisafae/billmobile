package com.example.billpromobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import com.example.billpromobile.data.local.preferences.AuthPreferences
import com.example.billpromobile.data.repository.AuthRepository
import com.example.billpromobile.databinding.ActivityLoginBinding
import com.example.billpromobile.ui.auth.viewmodel.AuthViewModel
import com.example.billpromobile.ui.auth.viewmodel.AuthViewModelFactory
import com.example.billpromobile.ui.employe.EmployeDashboard
import com.example.billpromobile.ui.manager.ManagerDashboard
import com.example.billpromobile.ui.superadmin.SuperAdminDashboard
import com.example.billpromobile.utils.NetworkUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authPreferences: AuthPreferences
    private val authRepository = AuthRepository()

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authPreferences = AuthPreferences(this)

        // Déjà connecté ?
        if (authPreferences.isLoggedIn()) {
            redirectToDashboard(authPreferences.getRole()!!)
            finish()
            return
        }

        // ViewModel
        val authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(authRepository)
        )[AuthViewModel::class.java]

        // Observer loginState
        lifecycleScope.launch {
            authViewModel.loginState.collectLatest { state ->
                when (state) {
                    is AuthViewModel.LoginState.Loading -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "Chargement...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is AuthViewModel.LoginState.Success -> {
                        val data = state.response
                        authPreferences.saveUser(data.token, data.role, data.nom, data.prenom)

                        Toast.makeText(
                            this@LoginActivity,
                            "Bienvenue ${data.prenom} ${data.nom}",
                            Toast.LENGTH_LONG
                        ).show()

                        redirectToDashboard(data.role)
                    }

                    is AuthViewModel.LoginState.Error -> {
                        Toast.makeText(
                            this@LoginActivity,
                            state.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is AuthViewModel.LoginState.Idle -> {
                        // Pas d'action nécessaire
                    }
                }
            }
        }

        // Gestion bouton Se connecter
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!NetworkUtils.isConnected(this)) {
                Toast.makeText(this, "Pas de connexion internet", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.login(email, password)
        }

        // 👁️ Gestion affichage / masquage du mot de passe
        binding.ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.etPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }

            binding.etPassword.setSelection(binding.etPassword.text.length)
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
