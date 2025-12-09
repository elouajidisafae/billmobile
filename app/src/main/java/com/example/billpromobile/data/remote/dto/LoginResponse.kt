package com.example.billpromobile.data.remote.dto

data class LoginResponse(
    val token: String,
    val role: String,
    val nom: String,
    val prenom: String
)