package com.example.gestintaller_ingenieriarequisitos.databases;

import static com.example.gestintaller_ingenieriarequisitos.utils.Constants.ARCHIVO_INICIALIZACION_SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "MyDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private final Context context;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Crear la tabla de usuarios
    @Override
    public void onCreate(SQLiteDatabase db) {
        ejecutarSQLDesdeArchivo(db,context);
    }

    // Actualizar la base de datos si se cambia la versión
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }

    private void dropAllTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS tUsuario");
        db.execSQL("DROP TABLE IF EXISTS tRol");
        db.execSQL("DROP TABLE IF EXISTS tPermiso");
        db.execSQL("DROP TABLE IF EXISTS tPiezas");
        db.execSQL("DROP TABLE IF EXISTS tTipoPieza");
    }

    private void ejecutarSQLDesdeArchivo(SQLiteDatabase db, Context context) {
        try {
            InputStream input = context.getAssets().open(ARCHIVO_INICIALIZACION_SQL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder sb = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                sb.append(linea);
            }
            reader.close();

            // Ejecutar las instrucciones SQL
            String[] comandos = sb.toString().split(";");
            for (String comando : comandos) {
                if (!comando.trim().isEmpty()) {
                    db.execSQL(comando);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
