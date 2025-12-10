package com.example.billpromobile.ui.employe

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.billpromobile.data.local.preferences.AuthPreferences
import com.example.billpromobile.databinding.ActivityEmployeDashboardBinding
import com.example.billpromobile.ui.auth.LoginActivity

class EmployeDashboard : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeDashboardBinding
    private lateinit var authPreferences: AuthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authPreferences = AuthPreferences(this)

        binding.tvWelcome.text = "Employé\n${authPreferences.getPrenom()} ${authPreferences.getNom()}"

        binding.btnLogout.setOnClickListener {
            authPreferences.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show()
        }
    }
}