package com.example.dogstore.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("users_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val usersKey = "users"

    // Получить всех пользователей
    fun getUsers(): MutableMap<String, String> {
        val json = prefs.getString(usersKey, null) ?: return mutableMapOf()
        val type = object : TypeToken<MutableMap<String, String>>() {}.type
        return gson.fromJson(json, type)
    }

    // Проверка существования пользователя
    fun userExists(username: String): Boolean {
        return getUsers().containsKey(username)
    }

    // Проверка логина
    fun validateCredentials(username: String, password: String): Boolean {
        val users = getUsers()
        return users[username] == password
    }

    // Добавление нового пользователя
    fun saveUser(username: String, password: String) {
        val users = getUsers()
        users[username] = password
        val json = gson.toJson(users)
        prefs.edit().putString(usersKey, json).apply()
    }
}
