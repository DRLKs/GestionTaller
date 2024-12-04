package com.example.gestintaller_ingenieriarequisitos.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mecanica.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;


    // Tablas
    public static final String TABLE_USUARIO = "tUsuario";
    public static final String TABLE_PIEZAS = "tPiezas";
    public static final String TABLE_TIPOS = "tTipoPieza";

    // Creación de tablas
    private static final String CREATE_TABLE_USUARIO =
            "CREATE TABLE " + TABLE_USUARIO + " (" +
                    "nombre TEXT PRIMARY KEY, " +
                    "password TEXT, " +
                    "rolName TEXT);";

    private static final String CREATE_TABLE_PIEZAS =
            "CREATE TABLE " + TABLE_PIEZAS + " (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NOMBRE TEXT, " +
                    "FABRICANTE TEXT, " +
                    "ID_TIPO TEXT);";

    private static final String CREATE_TABLE_TIPOS =
            "CREATE TABLE " + TABLE_TIPOS + " (" +
                    "ID_TIPO TEXT PRIMARY KEY, " +
                    "NOMBRE TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    * Función que crea la base de datos
    */
    @Override
    public void onCreate(SQLiteDatabase db) {
        ejecutarSQLDesdeArchivo(db, context, "init_data.sql");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIEZAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS);
        onCreate(db);
    }

    private void ejecutarSQLDesdeArchivo(SQLiteDatabase db, Context context, String archivo) {
        try {
            InputStream input = context.getAssets().open(archivo);
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
