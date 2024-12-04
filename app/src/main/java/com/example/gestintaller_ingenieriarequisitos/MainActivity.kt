package com.example.gestintaller_ingenieriarequisitos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestintaller_ingenieriarequisitos.databases.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instanciar la base de datos
        SQLiteDatabase db;
        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {
            db = dbHelper.getReadableDatabase();
        }

        // Botón para probar la conexión y consultar datos iniciales
        Button btnTestDatabase = findViewById(R.id.btnTestDatabase);
        btnTestDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Probar lectura de datos iniciales
                Cursor cursor = db.rawQuery("SELECT * FROM tUsuario", null);
                if (cursor.moveToFirst()) {
                    do {
                        String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                        String rol = cursor.getString(cursor.getColumnIndexOrThrow("rolName"));
                        Log.d(TAG, "Usuario: " + nombre + " - Rol: " + rol);
                    } while (cursor.moveToNext());
                } else {
                    Log.d(TAG, "No se encontraron usuarios en la base de datos.");
                }
                cursor.close();
            }
        });
    }
}
