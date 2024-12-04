package com.example.gestintaller_ingenieriarequisitos.databases;

import static com.example.gestintaller_ingenieriarequisitos.utils.Constants.*;

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
    private final Context context;


    // Tablas
    public static final String TABLE_USUARIO = "tUsuario";
    public static final String TABLE_PIEZAS = "tPiezas";
    public static final String TABLE_TIPOS = "tTipoPieza";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /*
    * Función que crea la base de datos
    */
    @Override
    public void onCreate(SQLiteDatabase db) {
        ejecutarSQLDesdeArchivo(db, context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIEZAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS);
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
