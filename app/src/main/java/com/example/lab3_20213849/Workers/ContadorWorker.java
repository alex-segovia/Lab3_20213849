package com.example.lab3_20213849.Workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ContadorWorker extends Worker {
    public ContadorWorker(Context context, WorkerParameters params){
        super(context,params);
    }

    @NonNull
    @Override
    public Result doWork() {
        int minutos = getInputData().getInt("minutos",0);
        int segundos = 0;

        while(minutos!=0 || segundos!=0){
            if(isStopped()){
                return Result.failure();
            }
            if(segundos==0){
                minutos--;
                segundos=59;
            }else{
                segundos--;
            }

            int contador = minutos*60+segundos;


            setProgressAsync(new Data.Builder().putInt("contador",contador).build());

            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
                return Result.failure();
            }
        }

        Data data = new Data.Builder()
                .putString("info","Worker finalizado")
                .build();


        return Result.success(data);
    }


}
