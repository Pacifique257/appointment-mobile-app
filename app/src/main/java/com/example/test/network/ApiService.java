package com.example.test.network;

import com.example.test.models.Availability;
import com.example.test.models.LoginRequest;
import com.example.test.models.LoginResponse;
import com.example.test.models.RegisterRequest;
import com.example.test.models.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("users/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("availabilities")
    Call<List<Availability>> getAvailabilities();
}
