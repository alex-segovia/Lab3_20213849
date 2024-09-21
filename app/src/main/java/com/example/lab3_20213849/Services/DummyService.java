package com.example.lab3_20213849.Services;

import com.example.lab3_20213849.Dtos.ToDo;
import com.example.lab3_20213849.Dtos.ToDoDto;
import com.example.lab3_20213849.Dtos.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DummyService {

    @FormUrlEncoded
    @POST("/auth/login")
    Call<Usuario> existeUsuario(@Field("username") String username, @Field("password") String password);

    @GET("/todos/user/{userId}")
    Call<ToDoDto> obtenerListaTareas(@Path("userId") Integer userId);
}
