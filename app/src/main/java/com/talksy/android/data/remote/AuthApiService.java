package com.talksy.android.data.remote;

import com.talksy.android.data.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthApiService {
    
    @POST("auth/signup")
    Call<User> signup(@Body SignupRequest request);
    
    @POST("auth/login")
    Call<User> login(@Body LoginRequest request);
    
    @POST("auth/logout")
    Call<Void> logout();
    
    @PUT("auth/update-profile")
    Call<User> updateProfile(@Body UpdateProfileRequest request);
    
    @GET("auth/check")
    Call<User> checkAuth();
}
