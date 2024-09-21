package com.example.lab3_20213849;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20213849.Dtos.ToDo;
import com.example.lab3_20213849.Dtos.Usuario;
import com.example.lab3_20213849.Services.DummyService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TareasActivity extends AppCompatActivity {

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tareas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        usuario = (Usuario)intent.getSerializableExtra("usuario");
        ToDo[] listaToDos = (ToDo[]) intent.getSerializableExtra("listaTareas");

        TextView textoTareas = findViewById(R.id.textoTareas);
        String nuevoTexto = "Ver tareas de " + usuario.getFirstName() + ":";
        textoTareas.setText(nuevoTexto);

        String[] listaSpinner = new String[listaToDos.length];
        int i = 0;
        for (ToDo todo: listaToDos){
            listaSpinner[i] = todo.getTodo() + " - " + (todo.isCompleted()?"Completado":"No completado");
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,listaSpinner);

        Spinner spinnerDatos = findViewById(R.id.spinnerDatos);
        spinnerDatos.setAdapter(adapter);

        Button botonCambiarEstado = findViewById(R.id.botonCambiarEstado);
        botonCambiarEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyService dummyService = new Retrofit.Builder()
                        .baseUrl("https://dummyjson.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(DummyService.class);

                dummyService.cambiarEstado(listaToDos[spinnerDatos.getSelectedItemPosition()].getId(),!listaToDos[spinnerDatos.getSelectedItemPosition()].isCompleted()).enqueue(new Callback<ToDo>() {
                    @Override
                    public void onResponse(Call<ToDo> call, Response<ToDo> response) {
                        if(response.isSuccessful()){
                            ToDo todoResponse = response.body();
                            Toast.makeText(TareasActivity.this,"Se cambió el estado de la tarea " + todoResponse.getTodo() + " a " + todoResponse.isCompleted(),Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(Call<ToDo> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pomodoro,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.botonCerrarSesion){
            Intent intent = new Intent(TareasActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}