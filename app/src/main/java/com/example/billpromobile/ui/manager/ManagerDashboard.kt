package com.example.billpromobile.ui.manager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.billpromobile.data.local.preferences.AuthPreferences
import com.example.billpromobile.databinding.ActivityManagerDashboardBinding
import com.example.billpromobile.ui.auth.LoginActivity

class ManagerDashboard : AppCompatActivity() {

    private lateinit var binding: ActivityManagerDashboardBinding
    private lateinit var authPreferences: AuthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authPreferences = AuthPreferences(this)

        binding.tvWelcome.text = "Manager\n${authPreferences.getPrenom()} ${authPreferences.getNom()}"

        binding.btnLogout.setOnClickListener {
            authPreferences.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show()
        }
    }
}