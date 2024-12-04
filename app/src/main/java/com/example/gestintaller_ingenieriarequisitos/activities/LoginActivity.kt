package com.example.gestintaller_ingenieriarequisitos.activities;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestintaller_ingenieriarequisitos.MainActivity
import com.example.gestintaller_ingenieriarequisitos.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referencias a los elementos de la UI
        val usernameEditText: EditText = findViewById(R.id.username)
        val passwordEditText: EditText = findViewById(R.id.password)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Lógica de autenticación (puedes adaptarla con tu base de datos)
            if (username == "admin" && password == "admin123") {
                // Si las credenciales son correctas, abre la MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Cierra LoginActivity para evitar que el usuario vuelva atrás
            } else {
                // Si las credenciales son incorrectas
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
