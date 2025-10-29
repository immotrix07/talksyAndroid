package com.talksy.android.services;

import com.talksy.android.models.Chat;
import com.talksy.android.models.LoginResponse;
import com.talksy.android.models.Message;
import com.talksy.android.models.SignupResponse;
import com.talksy.android.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/signup")
    Call<SignupResponse> signup(@Body User user);

    @POST("auth/login")
    Call<LoginResponse> login(@Body User user);

    @GET("chat")
    Call<List<Chat>> getChats(@Header("Authorization") String token);

    @POST("chat")
    Call<Chat> createOrGetChat(@Header("Authorization") String token, @Body Chat chat);

    @GET("messages/{chatId}")
    Call<List<Message>> getMessages(@Header("Authorization") String token, @Path("chatId") String chatId);

    @POST("messages")
    Call<Message> sendMessage(@Header("Authorization") String token, @Body Message message);
}
