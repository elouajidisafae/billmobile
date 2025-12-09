package com.example.billpromobile.data.local.preferences

import android.content.Context
import com.example.billpromobile.utils.Constants

class AuthPreferences(context: Context) {
    private val prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    fun saveUser(token: String, role: String, nom: String, prenom: String) {
        prefs.edit()
            .putString(Constants.KEY_TOKEN, token)
            .putString(Constants.KEY_ROLE, role)
            .putString(Constants.KEY_NOM, nom)
            .putString(Constants.KEY_PRENOM, prenom)
            .apply()
    }

    fun getToken() = prefs.getString(Constants.KEY_TOKEN, null)
    fun getRole() = prefs.getString(Constants.KEY_ROLE, null)
    fun getNom() = prefs.getString(Constants.KEY_NOM, "") ?: ""
    fun getPrenom() = prefs.getString(Constants.KEY_PRENOM, "") ?: ""

    fun isLoggedIn() = getToken() != null

    fun logout() = prefs.edit().clear().apply()
}