package com.example.gestintaller_ingenieriarequisitos.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestintaller_ingenieriarequisitos.MainActivityJavaAux;
import com.example.gestintaller_ingenieriarequisitos.R;
import com.example.gestintaller_ingenieriarequisitos.databases.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);

        SQLiteDatabase db;
        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {
            db = dbHelper.getReadableDatabase();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                Cursor cursor = db.rawQuery("SELECT rolName FROM tUsuario WHERE nombre = ? AND password = ?",
                        new String[]{user, pass});

                if (cursor.moveToFirst()) {
                    String rol = cursor.getString(0);
                    Intent intent = new Intent(LoginActivity.this, MainActivityJavaAux.class);
                    intent.putExtra("rol", rol);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });
    }
}
