package com.example.gestintaller_ingenieriarequisitos.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.gestintaller_ingenieriarequisitos.MainActivity
import com.example.gestintaller_ingenieriarequisitos.R
import com.example.gestintaller_ingenieriarequisitos.databases.DatabaseHelper

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Ajusta el nombre del layout si es diferente

        val usernameField: EditText = findViewById(R.id.username)
        val passwordField: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login)
        val cancelButton: Button = findViewById(R.id.cancel)

        // Instancia del helper de base de datos
        val dbHelper = DatabaseHelper(this)

        // Activar el botón solo si hay texto en ambos campos
        usernameField.addTextChangedListener {
            loginButton.isEnabled = usernameField.text.isNotEmpty() && passwordField.text.isNotEmpty()
        }

        passwordField.addTextChangedListener {
            loginButton.isEnabled = usernameField.text.isNotEmpty() && passwordField.text.isNotEmpty()
        }

        // Acción del botón de inicio de sesión
        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            val rolName = authenticateUser(dbHelper, username, password)

            if (rolName != null) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                // Aquí puedes navegar a otra actividad o pantalla
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("rolName", rolName) // Pasa el rol del usuario a la siguiente actividad
                startActivity(intent)
                finish()  // Cierra LoginActivity después de iniciar MainActivity
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            // Cierra la aplicación
            finishAffinity() // Cierra la actividad y todas las que estén en la pila
        }

    }

    /**
     * Función para autenticar al usuario con la base de datos.
     * Devuelve `rolName` si el usuario y la contraseña coinciden, de lo contrario, null.
     */
    private fun authenticateUser(dbHelper: DatabaseHelper, username: String, password: String): String? {
        val db = dbHelper.readableDatabase
        val query = "SELECT rolName FROM tUsuario WHERE nombre = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        var rolName: String? = null
        if (cursor.moveToFirst()) {
            rolName = cursor.getString(cursor.getColumnIndexOrThrow("rolName"))
        }
        cursor.close()
        db.close()
        return rolName
    }

}


