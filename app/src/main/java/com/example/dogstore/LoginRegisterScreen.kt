package com.example.dogstore.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.dogstore.data.UserStore

@Composable
fun LoginRegisterScreen(onLoginSuccess: () -> Unit) {
    // Field states
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Entire layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Name field
            OutlinedTextField(
                value = username,
                onValueChange = { newValue ->
                    if (newValue.length <= 8) {
                        username = newValue
                    }
                },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field, only numbers, maximum 8 characters
            OutlinedTextField(
                value = password,
                onValueChange = { newValue ->
                    if (newValue.length <= 8 && newValue.all { it.isDigit() }) {
                        password = newValue
                    }
                },
                label = { Text("Password (numbers only)") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current
            val userStore = remember { UserStore(context) }

            // Registration check
            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Enter username and password", Toast.LENGTH_SHORT).show()
                    } else if (userStore.userExists(username)) {
                        Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        userStore.saveUser(username, password)
                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                        username = ""
                        password = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login check
            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Enter username and password", Toast.LENGTH_SHORT).show()
                    } else if (userStore.validateCredentials(username, password)) {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        onLoginSuccess() // navigate to MainScreen
                    } else {
                        Toast.makeText(
                            context,
                            "Invalid username or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }
}
