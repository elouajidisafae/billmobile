package com.example.billpromobile.ui.superadmin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.billpromobile.data.local.preferences.AuthPreferences
import com.example.billpromobile.databinding.ActivitySuperadminDashboardBinding
import com.example.billpromobile.ui.auth.LoginActivity

class SuperAdminDashboard : AppCompatActivity() {
    private lateinit var binding: ActivitySuperadminDashboardBinding
    private lateinit var authPreferences: AuthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuperadminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authPreferences = AuthPreferences(this)

        binding.tvWelcome.text = "Super Admin\n${authPreferences.getPrenom()} ${authPreferences.getNom()}"

        binding.btnLogout.setOnClickListener {
            authPreferences.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}