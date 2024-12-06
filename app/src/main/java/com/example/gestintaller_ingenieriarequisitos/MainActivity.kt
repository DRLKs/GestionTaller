package com.example.gestintaller_ingenieriarequisitos

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.gestintaller_ingenieriarequisitos.databases.DatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var btnInsertar: Button
    private lateinit var btnBorrar: Button
    private lateinit var btnModificar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var btnSalir: Button
    private lateinit var etNombrePieza: EditText
    private lateinit var etFabricante: EditText
    private lateinit var listTiposPiezas: ListView
    private lateinit var gridPiezas: GridView
    private lateinit var userRole: String
    val dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener el rol del usuario desde el Intent
        userRole = intent.getStringExtra("rolName") ?: "Invitado"
        Log.d("MainActivity", "Rol del usuario: $userRole")

        // Inicialización de vistas
        btnSalir = findViewById(R.id.btnSalir)
        btnLimpiar = findViewById(R.id.button5)
        btnInsertar = findViewById(R.id.btnInsertar)
        btnBorrar = findViewById(R.id.btnBorrar)
        btnModificar = findViewById(R.id.btnActualizar)
        etNombrePieza = findViewById(R.id.etNombrePieza)
        etFabricante = findViewById(R.id.etFabricante)
        listTiposPiezas = findViewById(R.id.listTiposPiezas)
        gridPiezas = findViewById(R.id.gridPiezas)

        // Obtener los tipos de piezas desde la base de datos
        val tipos = obtenerTiposPiezas()

        // Crear un adaptador para el ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tipos)

        // Asignar el adaptador al ListView
        listTiposPiezas.adapter = adapter

        listTiposPiezas.setOnItemClickListener { _, _, position, _ ->
            val tipoSeleccionado = tipos[position]
            val tipoCodigo = obtenerCodigoDeTipo(tipoSeleccionado)
            Log.d("MainActivity", "Tipo seleccionado: $tipoSeleccionado con código: $tipoCodigo") // Verificar valor seleccionado
            cargarPiezasPorTipo(tipoCodigo) // Cargar las piezas del tipo seleccionado usando el código
        }

        gridPiezas.setOnItemClickListener { _, _, position, _ ->
            val adapter = gridPiezas.adapter
            if (adapter is PiezaAdapter) {
                val piezaSeleccionada = adapter.getItem(position) as Pieza
                cargarDatosPieza(piezaSeleccionada)
            } else {
                Toast.makeText(this, "Error al seleccionar la pieza.", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar botones
        btnSalir.setOnClickListener { finish() }
        btnLimpiar.setOnClickListener {
            etNombrePieza.text.clear()
            etFabricante.text.clear()
            gridPiezas.clearChoices()
            listTiposPiezas.clearChoices()
        }

        // Habilitar/Deshabilitar botones según el rol del usuario
        if (userRole != "administrador") {
            btnInsertar.isEnabled = false
            btnModificar.isEnabled = false
            btnBorrar.isEnabled = false
        } else {
            btnInsertar.setOnClickListener { insertarPieza() }
            btnBorrar.setOnClickListener { borrarPieza() }
            btnModificar.setOnClickListener { modificarPieza() }
        }
    }

    private fun obtenerPiezasPorTipo(tipo: String): List<Pieza> {
        val piezas = mutableListOf<Pieza>()
        val db = dbHelper.readableDatabase

        // Aquí buscamos el código del tipo
        val query = "SELECT ID, NOMBRE, FABRICANTE, ID_TIPO FROM tPiezas WHERE ID_TIPO = ?"
        val cursor = db.rawQuery(query, arrayOf(tipo)) // Pasar el código del tipo (como 'A', 'B', etc.)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE"))
                val fabricante = cursor.getString(cursor.getColumnIndexOrThrow("FABRICANTE"))
                val idTipo = cursor.getString(cursor.getColumnIndexOrThrow("ID_TIPO"))
                piezas.add(Pieza(id, nombre, fabricante, idTipo))
            } while (cursor.moveToNext())
        } else {
            Log.d("MainActivity", "No se encontraron piezas para el tipo: $tipo")
        }

        cursor.close()
        db.close()
        return piezas
    }

    private fun obtenerCodigoDeTipo(nombreTipo: String): String {
        val db = dbHelper.readableDatabase
        val query = "SELECT ID_TIPO FROM tTipoPieza WHERE NOMBRE = ?"
        val cursor = db.rawQuery(query, arrayOf(nombreTipo))

        var tipoCodigo = ""
        if (cursor.moveToFirst()) {
            tipoCodigo = cursor.getString(cursor.getColumnIndexOrThrow("ID_TIPO"))
        }

        cursor.close()
        db.close()
        return tipoCodigo
    }

    private fun obtenerTiposPiezas(): List<String> {
        val tipos = mutableListOf<String>()
        val db = dbHelper.readableDatabase

        val query = "SELECT NOMBRE, ID_TIPO FROM tTipoPieza"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE"))
                tipos.add(nombre)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return tipos
    }

    private fun cargarDatosPieza(pieza: Pieza) {
        etNombrePieza.setText(pieza.nombre)
        etFabricante.setText(pieza.fabricante)
    }

    private fun cargarPiezasPorTipo(tipo: String) {
        Log.d("MainActivity", "Tipo seleccionado: $tipo") // Agregar log para verificar el valor
        val piezas = obtenerPiezasPorTipo(tipo) // Recuperar piezas de la base de datos

        if (piezas.isEmpty()) {
            Toast.makeText(this, "No se encontraron piezas para el tipo seleccionado.", Toast.LENGTH_SHORT).show()
            gridPiezas.adapter = null // Limpiar el adaptador
        } else {
            // Crear un nuevo adaptador para el GridView
            val adapter = PiezaAdapter(this, piezas)
            gridPiezas.adapter = adapter
        }
    }

    private fun insertarPieza() {

    }

    private fun borrarPieza() {

    }

    private fun modificarPieza() {

    }

    // Clase de datos para las piezas
    data class Pieza(
        val id: Int,
        val nombre: String,
        val fabricante: String,
        val idTipo: String
    )
}





