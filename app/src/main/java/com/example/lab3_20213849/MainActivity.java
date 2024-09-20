package com.example.lab3_20213849;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20213849.Dtos.Usuario;
import com.example.lab3_20213849.Services.DummyService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText textoUsuario = findViewById(R.id.usuario);
        EditText textoContrasena = findViewById(R.id.contrasena);
        Button botonIniciarSesion = findViewById(R.id.botonIniciarSesion);
        botonIniciarSesion.setClickable(false);
        botonIniciarSesion.getBackground().setAlpha(128);

        textoUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty() && !textoContrasena.getText().toString().trim().isEmpty()){
                    botonIniciarSesion.setClickable(true);
                    botonIniciarSesion.getBackground().setAlpha(255);
                }else {
                    botonIniciarSesion.setClickable(false);
                    botonIniciarSesion.getBackground().setAlpha(128);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textoContrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty() && !textoUsuario.getText().toString().trim().isEmpty()){
                    botonIniciarSesion.setClickable(true);
                    botonIniciarSesion.getBackground().setAlpha(255);
                }else {
                    botonIniciarSesion.setClickable(false);
                    botonIniciarSesion.getBackground().setAlpha(128);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        DummyService dummyService = new Retrofit.Builder()
                .baseUrl("https://dummyjson.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DummyService.class);

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dummyService.existeUsuario(textoUsuario.getText().toString(),textoContrasena.getText().toString()).enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (response.isSuccessful()){
                            Usuario usuario = response.body();
                            Log.d("usuarioId",usuario.getId().toString());
                            Log.d("usuarioUsername",usuario.getUsername());
                            Intent intent = new Intent(MainActivity.this, PomodoroActivity.class);
                            intent.putExtra("usuario",usuario);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"El usuario y la contraseña ingresados no existe",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }
}