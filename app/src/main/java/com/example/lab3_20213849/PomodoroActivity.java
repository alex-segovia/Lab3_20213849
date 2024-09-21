package com.example.lab3_20213849;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.lab3_20213849.Dtos.ToDo;
import com.example.lab3_20213849.Dtos.ToDoDto;
import com.example.lab3_20213849.Dtos.Usuario;
import com.example.lab3_20213849.Services.DummyService;
import com.example.lab3_20213849.Workers.ContadorWorker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PomodoroActivity extends AppCompatActivity {

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pomodoro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        usuario = (Usuario)intent.getSerializableExtra("usuario");

        TextView nombreUsuario = findViewById(R.id.nombresApellidos);
        String nuevoNombre = usuario.getFirstName() + " " + usuario.getLastName();
        nombreUsuario.setText(nuevoNombre);

        ImageView icono = findViewById(R.id.iconoGenero);
        if(usuario.getGender().equals("male")){
            icono.setImageResource(R.drawable.baseline_man_24);
        }else{
            icono.setImageResource(R.drawable.baseline_woman_24);
        }

        TextView correoElectronico = findViewById(R.id.correoElectronico);
        correoElectronico.setText(usuario.getEmail());

        ImageButton botonReiniciarCuenta = findViewById(R.id.botonReiniciarCuenta);

        ImageButton botonIniciarPomodoro = findViewById(R.id.botonIniciarPomodoro);

        TextView textoContador = findViewById(R.id.textoContador);

        TextView textoInfoDescanso = findViewById(R.id.textoInfoDescanso);

        botonIniciarPomodoro.setOnClickListener(view -> {
            botonIniciarPomodoro.setVisibility(View.GONE);
            botonReiniciarCuenta.setVisibility(View.VISIBLE);

            Data dataBuilder = new Data.Builder()
                    .putInt("minutos", 25)
                    .build();

            WorkRequest workRequest = new OneTimeWorkRequest.Builder(ContadorWorker.class)
                    .setInputData(dataBuilder)
                    .build();

            WorkManager
                    .getInstance(this)
                    .enqueue(workRequest);

            WorkManager.getInstance(this)
                    .getWorkInfoByIdLiveData(workRequest.getId())
                    .observe(PomodoroActivity.this, workInfo -> {
                        if(workInfo!=null){
                            if (workInfo.getState()== WorkInfo.State.RUNNING){
                                Data progress = workInfo.getProgress();
                                int contador = progress.getInt("contador",0);
                                int nuevosMinutos = contador/60;
                                int nuevosSegundos = contador%60;
                                String nuevosMinutosStr = nuevosMinutos<10?"0"+String.valueOf(nuevosMinutos):String.valueOf(nuevosMinutos);
                                String nuevosSegundosStr = nuevosSegundos<10?"0"+String.valueOf(nuevosSegundos):String.valueOf(nuevosSegundos);
                                String nuevoTexto = nuevosMinutosStr+":"+nuevosSegundosStr;
                                textoContador.setText(nuevoTexto);
                            }else if (workInfo.getState() == WorkInfo.State.SUCCEEDED){
                                textoInfoDescanso.setText("En descanso");

                                Data dataBuilderDescanso = new Data.Builder()
                                        .putInt("minutos", 5)
                                        .build();

                                WorkRequest workRequestDescanso = new OneTimeWorkRequest.Builder(ContadorWorker.class)
                                        .setInputData(dataBuilderDescanso)
                                        .build();

                                WorkManager
                                        .getInstance(this)
                                        .enqueue(workRequestDescanso);

                                WorkManager.getInstance(this)
                                        .getWorkInfoByIdLiveData(workRequestDescanso.getId())
                                        .observe(PomodoroActivity.this,workInfoDescanso -> {
                                            if(workInfoDescanso!=null){
                                                if(workInfoDescanso.getState() == WorkInfo.State.RUNNING){
                                                    Data progressDescanso = workInfoDescanso.getProgress();
                                                    int contadorDescanso = progressDescanso.getInt("contador",0);
                                                    int nuevosMinutosDescanso = contadorDescanso/60;
                                                    int nuevosSegundosDescanso = contadorDescanso%60;
                                                    String nuevosMinutosDescansoStr = nuevosMinutosDescanso<10?"0"+String.valueOf(nuevosMinutosDescanso):String.valueOf(nuevosMinutosDescanso);
                                                    String nuevosSegundosDescansoStr = nuevosSegundosDescanso<10?"0"+String.valueOf(nuevosSegundosDescanso):String.valueOf(nuevosSegundosDescanso);
                                                    String nuevoTexto = nuevosMinutosDescansoStr+":"+nuevosSegundosDescansoStr;
                                                    textoContador.setText(nuevoTexto);
                                                }else if (workInfoDescanso.getState() == WorkInfo.State.SUCCEEDED){
                                                    textoInfoDescanso.setText("Fin del descanso");
                                                    textoContador.setText("00:00");

                                                    MaterialAlertDialogBuilder dialogFinDescanso = new MaterialAlertDialogBuilder(PomodoroActivity.this);
                                                    dialogFinDescanso.setTitle("Atención");
                                                    dialogFinDescanso.setMessage("Terminó el tiempo de descanso. Dale al botón de reinicio para empezar otro ciclo");
                                                    dialogFinDescanso.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    });
                                                    dialogFinDescanso.show();
                                                }
                                            }
                                        });

                                DummyService dummyService = new Retrofit.Builder()
                                        .baseUrl("https://dummyjson.com")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build()
                                        .create(DummyService.class);

                                dummyService.obtenerListaTareas(usuario.getId()).enqueue(new Callback<ToDoDto>() {
                                    @Override
                                    public void onResponse(Call<ToDoDto> call, Response<ToDoDto> response) {
                                        if (response.isSuccessful()){
                                            ToDoDto toDoDto = response.body();
                                            if (toDoDto.getTodos().length==0){
                                                MaterialAlertDialogBuilder dialogInicioDescanso = new MaterialAlertDialogBuilder(PomodoroActivity.this);
                                                dialogInicioDescanso.setTitle("¡Felicidades!");
                                                dialogInicioDescanso.setMessage("Empezó el tiempo de descanso!");
                                                dialogInicioDescanso.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });
                                                dialogInicioDescanso.show();
                                            }else{
                                                Intent intent = new Intent(PomodoroActivity.this, TareasActivity.class);
                                                intent.putExtra("listaTareas",toDoDto.getTodos());
                                                intent.putExtra("usuario",usuario);
                                                launcher.launch(intent);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ToDoDto> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    });
        });

        botonReiniciarCuenta.setOnClickListener(view -> {
            WorkManager.getInstance(this)
                    .cancelAllWork();

            textoInfoDescanso.setText("Descanso: 05:00");

            Data dataBuilder = new Data.Builder()
                    .putInt("minutos", 25)
                    .build();

            WorkRequest workRequest = new OneTimeWorkRequest.Builder(ContadorWorker.class)
                    .setInputData(dataBuilder)
                    .build();

            WorkManager
                    .getInstance(this)
                    .enqueue(workRequest);

            WorkManager.getInstance(this)
                    .getWorkInfoByIdLiveData(workRequest.getId())
                    .observe(PomodoroActivity.this, workInfo -> {
                        if(workInfo!=null){
                            if (workInfo.getState()== WorkInfo.State.RUNNING){
                                Data progress = workInfo.getProgress();
                                int contador = progress.getInt("contador",0);
                                int nuevosMinutos = contador/60;
                                int nuevosSegundos = contador%60;
                                String nuevosMinutosStr = nuevosMinutos<10?"0"+String.valueOf(nuevosMinutos):String.valueOf(nuevosMinutos);
                                String nuevosSegundosStr = nuevosSegundos<10?"0"+String.valueOf(nuevosSegundos):String.valueOf(nuevosSegundos);
                                String nuevoTexto = nuevosMinutosStr+":"+nuevosSegundosStr;
                                textoContador.setText(nuevoTexto);
                            }else if (workInfo.getState() == WorkInfo.State.SUCCEEDED){

                                textoInfoDescanso.setText("En descanso");

                                Data dataBuilderDescanso = new Data.Builder()
                                        .putInt("minutos", 5)
                                        .build();

                                WorkRequest workRequestDescanso = new OneTimeWorkRequest.Builder(ContadorWorker.class)
                                        .setInputData(dataBuilderDescanso)
                                        .build();

                                WorkManager
                                        .getInstance(this)
                                        .enqueue(workRequestDescanso);

                                WorkManager.getInstance(this)
                                        .getWorkInfoByIdLiveData(workRequestDescanso.getId())
                                        .observe(PomodoroActivity.this,workInfoDescanso -> {
                                            if(workInfoDescanso!=null){
                                                if(workInfoDescanso.getState() == WorkInfo.State.RUNNING){
                                                    Data progressDescanso = workInfoDescanso.getProgress();
                                                    int contadorDescanso = progressDescanso.getInt("contador",0);
                                                    int nuevosMinutosDescanso = contadorDescanso/60;
                                                    int nuevosSegundosDescanso = contadorDescanso%60;
                                                    String nuevosMinutosDescansoStr = nuevosMinutosDescanso<10?"0"+String.valueOf(nuevosMinutosDescanso):String.valueOf(nuevosMinutosDescanso);
                                                    String nuevosSegundosDescansoStr = nuevosSegundosDescanso<10?"0"+String.valueOf(nuevosSegundosDescanso):String.valueOf(nuevosSegundosDescanso);
                                                    String nuevoTexto = nuevosMinutosDescansoStr+":"+nuevosSegundosDescansoStr;
                                                    textoContador.setText(nuevoTexto);
                                                }else if (workInfoDescanso.getState() == WorkInfo.State.SUCCEEDED){
                                                    textoInfoDescanso.setText("Fin del descanso");
                                                    textoContador.setText("00:00");

                                                    MaterialAlertDialogBuilder dialogFinDescanso = new MaterialAlertDialogBuilder(PomodoroActivity.this);
                                                    dialogFinDescanso.setTitle("Atención");
                                                    dialogFinDescanso.setMessage("Terminó el tiempo de descanso. Dale al botón de reinicio para empezar otro ciclo");
                                                    dialogFinDescanso.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    });
                                                    dialogFinDescanso.show();
                                                }
                                            }
                                        });

                                DummyService dummyService = new Retrofit.Builder()
                                        .baseUrl("https://dummyjson.com")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build()
                                        .create(DummyService.class);

                                dummyService.obtenerListaTareas(usuario.getId()).enqueue(new Callback<ToDoDto>() {
                                    @Override
                                    public void onResponse(Call<ToDoDto> call, Response<ToDoDto> response) {
                                        if (response.isSuccessful()){
                                            ToDoDto toDoDto = response.body();
                                            if (toDoDto.getTodos().length==0){
                                                MaterialAlertDialogBuilder dialogInicioDescanso = new MaterialAlertDialogBuilder(PomodoroActivity.this);
                                                dialogInicioDescanso.setTitle("¡Felicidades!");
                                                dialogInicioDescanso.setMessage("Empezó el tiempo de descanso!");
                                                dialogInicioDescanso.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });
                                                dialogInicioDescanso.show();
                                            }else{
                                                Intent intent = new Intent(PomodoroActivity.this, TareasActivity.class);
                                                intent.putExtra("listaTareas",toDoDto.getTodos());
                                                intent.putExtra("usuario",usuario);
                                                launcher.launch(intent);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ToDoDto> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    });
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
            WorkManager.getInstance(PomodoroActivity.this)
                    .cancelAllWork();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            Intent datos = o.getData();
            usuario = (Usuario) datos.getSerializableExtra("usuario");
        }
    });

    @Override
    public void onBackPressed() {
        WorkManager.getInstance(PomodoroActivity.this)
                .cancelAllWork();
        super.onBackPressed();
    }
}