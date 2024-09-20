package com.example.lab3_20213849.Services;

import com.example.lab3_20213849.Dtos.Usuario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DummyService {

    @FormUrlEncoded
    @POST("/auth/login")
    Call<Usuario> existeUsuario(@Field("username") String username, @Field("password") String password);
}
