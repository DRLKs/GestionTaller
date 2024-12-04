package com.example.gestintaller_ingenieriarequisitos;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestintaller_ingenieriarequisitos.databases.DatabaseHelper;

public class MainActivityJavaAux extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula el diseño de login como la pantalla inicial
        setContentView(R.layout.activity_login);

        // Inicializa la base de datos
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        try {
            // Prueba la conexión a la base de datos
            dbHelper.getReadableDatabase();
            Log.d(TAG, "Base de datos inicializada correctamente.");
        } catch (Exception e) {
            Log.e(TAG, "Error al inicializar la base de datos", e);
        }
    }
}
