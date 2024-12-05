package com.example.gestintaller_ingenieriarequisitos

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestintaller_ingenieriarequisitos.databases.DatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var btnInsertar: Button
    private lateinit var btnBorrar: Button
    private lateinit var btnModificar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var btnSalir: Button
    private lateinit var etId: EditText
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var listTiposPiezas: ListView
    private lateinit var gridPiezas: GridView
    private lateinit var userRole: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener el rol del usuario desde el Intent
        userRole = intent.getStringExtra("rolName") ?: "Invitado"
        Log.d("MainActivity", "Rol del usuario: $userRole")
    }
}

