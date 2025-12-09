package com.example.billpromobile.data.repository

import com.example.billpromobile.data.remote.RetrofitClient
import com.example.billpromobile.data.remote.dto.LoginRequest

class AuthRepository {
    private val api = RetrofitClient.apiService

    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(email, password))
}