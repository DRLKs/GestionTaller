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

    // Nombre de la tabla y columnas
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Crear la tabla de usuarios
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE);
    }

    // Actualizar la base de datos si se cambia la versión
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
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
