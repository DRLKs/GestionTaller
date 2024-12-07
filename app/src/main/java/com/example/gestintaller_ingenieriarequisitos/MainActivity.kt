package com.example.gestintaller_ingenieriarequisitos

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
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
    private var tipoSeleccionado : String = ""
    private var piezaSeleccionadaId: Int? = null
    private val dbHelper = DatabaseHelper(this)

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
            tipoSeleccionado = tipos[position]
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
            gridPiezas.adapter = null
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
        piezaSeleccionadaId = pieza.id
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
        val nombrePieza = etNombrePieza.text.toString().trim()
        val fabricante = etFabricante.text.toString().trim()

        // Verificar que los campos no estén vacíos
        if (nombrePieza.isEmpty() || fabricante.isEmpty() || tipoSeleccionado.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el código del tipo de pieza usando el nombre del tipo
        val tipoCodigo = obtenerCodigoDeTipo(tipoSeleccionado)
        if (tipoCodigo.isEmpty()) {
            Toast.makeText(this, "El tipo de pieza seleccionado no es válido.", Toast.LENGTH_SHORT).show()
            return
        }

        // Insertar la nueva pieza en la base de datos
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("NOMBRE", nombrePieza)
            put("FABRICANTE", fabricante)
            put("ID_TIPO", tipoCodigo)
        }

        // Realizar la inserción
        val newRowId = db.insert("tPiezas", null, values)
        db.close()

        if (newRowId != -1L) {
            Toast.makeText(this, "Pieza insertada con éxito.", Toast.LENGTH_SHORT).show()
            // Limpiar los campos de texto
            etNombrePieza.text.clear()
            etFabricante.text.clear()

            // Actualizar el GridView para mostrar la nueva pieza
            cargarPiezasPorTipo(tipoCodigo)
        } else {
            Toast.makeText(this, "Error al insertar la pieza.", Toast.LENGTH_SHORT).show()
        }
    }

// Dentro de la clase MainActivity

    private fun borrarPieza() {
        val nombrePieza = etNombrePieza.text.toString().trim()
        val fabricante = etFabricante.text.toString().trim()

        // Verificar que los campos no estén vacíos
        if (nombrePieza.isEmpty() || fabricante.isEmpty() || tipoSeleccionado.isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona una pieza para borrar.", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el código del tipo de pieza usando el nombre del tipo
        val tipoCodigo = obtenerCodigoDeTipo(tipoSeleccionado)
        if (tipoCodigo.isEmpty()) {
            Toast.makeText(this, "El tipo de pieza seleccionado no es válido.", Toast.LENGTH_SHORT).show()
            return
        }

        // Borrar la pieza de la base de datos
        val db = dbHelper.writableDatabase
        val selection = "NOMBRE = ? AND FABRICANTE = ? AND ID_TIPO = ?"
        val selectionArgs = arrayOf(nombrePieza, fabricante, tipoCodigo)

        val deletedRows = db.delete("tPiezas", selection, selectionArgs)
        db.close()

        if (deletedRows > 0) {
            Toast.makeText(this, "Pieza borrada con éxito.", Toast.LENGTH_SHORT).show()
            // Limpiar los campos de texto
            etNombrePieza.text.clear()
            etFabricante.text.clear()

            // Actualizar el GridView para mostrar la pieza eliminada
            cargarPiezasPorTipo(tipoCodigo)
        } else {
            Toast.makeText(this, "Error al borrar la pieza.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun modificarPieza() {
        val nombrePieza = etNombrePieza.text.toString().trim()
        val fabricante = etFabricante.text.toString().trim()

        // Verificar que los campos no estén vacíos
        if (nombrePieza.isEmpty() || fabricante.isEmpty() || piezaSeleccionadaId == null) {
            Toast.makeText(this, "Por favor, selecciona una pieza y completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Actualizar la pieza en la base de datos
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("NOMBRE", nombrePieza)
            put("FABRICANTE", fabricante)
        }
        val selection = "ID = ?"
        val selectionArgs = arrayOf(piezaSeleccionadaId.toString())

        val count = db.update(
            "tPiezas",
            values,
            selection,
            selectionArgs
        )
        db.close()

        if (count > 0) {
            Toast.makeText(this, "Pieza modificada con éxito.", Toast.LENGTH_SHORT).show()
            // Limpiar los campos de texto
            etNombrePieza.text.clear()
            etFabricante.text.clear()
            piezaSeleccionadaId = null

            // Actualizar el GridView para mostrar la pieza modificada
            cargarPiezasPorTipo(obtenerCodigoDeTipo(tipoSeleccionado))
        } else {
            Toast.makeText(this, "Error al modificar la pieza.", Toast.LENGTH_SHORT).show()
        }
    }





    // Clase de datos para las piezas
    data class Pieza(
        val id: Int,
        val nombre: String,
        val fabricante: String,
        val idTipo: String
    )
}





