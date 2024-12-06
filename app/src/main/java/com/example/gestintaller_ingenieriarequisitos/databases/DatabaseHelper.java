package com.example.gestintaller_ingenieriarequisitos.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "TrabajoGI2425.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear las tablas
        db.execSQL("CREATE TABLE IF NOT EXISTS tRol (" +
                "rolName VARCHAR(50) PRIMARY KEY, " +
                "rolDes VARCHAR(255), " +
                "admin BIT NOT NULL);");

        db.execSQL("CREATE TABLE IF NOT EXISTS tTipoPieza (" +
                "ID_TIPO VARCHAR(4) PRIMARY KEY, " +
                "NOMBRE VARCHAR(80) NOT NULL);");

        db.execSQL("CREATE TABLE IF NOT EXISTS tPiezas (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NOMBRE VARCHAR(255) NOT NULL, " +
                "FABRICANTE VARCHAR(255) NOT NULL, " +
                "ID_TIPO VARCHAR(4) NOT NULL, " +
                "FOREIGN KEY(ID_TIPO) REFERENCES tTipoPieza(ID_TIPO) ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE IF NOT EXISTS tUsuario (" +
                "nombre VARCHAR(50) PRIMARY KEY, " +
                "password VARCHAR(50) NOT NULL, " +
                "rolName VARCHAR(50) NOT NULL, " +
                "FOREIGN KEY(rolName) REFERENCES tRol(rolName) ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE IF NOT EXISTS tPermiso (" +
                "rolName VARCHAR(50) NOT NULL, " +
                "pantalla VARCHAR(50) NOT NULL, " +
                "acceso BIT NOT NULL, " +
                "modificacion BIT NOT NULL, " +
                "PRIMARY KEY(rolName, pantalla), " +
                "FOREIGN KEY(rolName) REFERENCES tRol(rolName));");

            // Insertar datos iniciales
            db.execSQL("INSERT INTO tRol (rolName, rolDes, admin) VALUES" +
                    "('administrador', 'administrador', 1)," +
                    "('usuario', 'usuario', 0)," +
                    "('invitado', 'invitado', 0);");

            db.execSQL("INSERT INTO tTipoPieza (ID_TIPO, NOMBRE) VALUES" +
                    "('A', 'Chapa')," +
                    "('B', 'Motor')," +
                    "('C', 'Iluminación')," +
                    "('D', 'Sensores')," +
                    "('E', 'Cristales')," +
                    "('F', 'Pintura')," +
                    "('G', 'Otros');");

        db.execSQL("INSERT INTO tPiezas (NOMBRE, FABRICANTE, ID_TIPO) VALUES" +
                "('PARAGOLPES DELANTERO NEGRO-LISO A IMPRIMAR', 'MAZDA', 'A')," +
                "('PARAGOLPES TRASERO-IMPRIMADO', 'MAZDA', 'A')," +
                "('REJILLA NEGRA', 'MAZDA', 'A')," +
                "('ALETA DELANTERA DCH CON AUJERO PARA PILOTO CX3 16', 'MAZDA', 'A')," +
                "('ALETA DELANTERA IZQ CON AUJERO PARA PILOTO CX3 16', 'MAZDA', 'A')," +
                "('Bombillas luz delantera', 'RENAULT', 'C')," +
                "('Bombillas señalización delantera', 'RENAULT', 'C')," +
                "('Bombillas luz trasera', 'RENAULT', 'C')," +
                "('Bombillas señalización trasera', 'RENAULT', 'C')," +
                "('Estuches de bombillas', 'RENAULT', 'C')," +
                "('Iluminación LED', 'RENAULT', 'C')," +
                "('Bombillas interior', 'RENAULT', 'C')," +
                "('Bombillas Xenon', 'RENAULT', 'C')," +
                "('Juntas y otras piezas del motor', 'FORD', 'B')," +
                "('Alimentación', 'FORD', 'B')," +
                "('Kits de distribución', 'FORD', 'B')," +
                "('Correas', 'FORD', 'B')," +
                "('Poleas', 'FORD', 'B')," +
                "('Kits', 'FORD', 'B')," +
                "('Válvulas EGR', 'FORD', 'B')," +
                "('Herramienta específica', 'FORD', 'B')," +
                "('Turbocompresores', 'FORD', 'B')," +
                "('Sensores electrónicos y medidores de flujo', 'FORD', 'B')," +
                "('Cable de acelerador y starter', 'FORD', 'B');");

            db.execSQL("INSERT INTO tUsuario (nombre, password, rolName) VALUES" +
                    "('admin', 'admin', 'administrador')," +
                    "('user', 'user', 'usuario')," +
                    "('inv', 'inv', 'invitado');");
        }

    private void executeSqlScript(SQLiteDatabase db, String scriptName) throws IOException {
        InputStream inputStream = context.getAssets().open(scriptName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sqlScript = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sqlScript.append(line).append("\n");
        }
        reader.close();

        try {
            db.execSQL(sqlScript.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si la versión de la base de datos cambia, eliminamos las tablas y las recreamos.
        dropAllTables(db);
        onCreate(db);
    }

    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS tUsuario");
        db.execSQL("DROP TABLE IF EXISTS tRol");
        db.execSQL("DROP TABLE IF EXISTS tPermiso");
        db.execSQL("DROP TABLE IF EXISTS tPiezas");
        db.execSQL("DROP TABLE IF EXISTS tTipoPieza");
    }
}

