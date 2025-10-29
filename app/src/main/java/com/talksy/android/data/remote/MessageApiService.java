package com.talksy.android.data.remote;

import com.talksy.android.data.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageApiService {
    
    @GET("messages/contacts")
    Call<List<User>> getAllContacts();
    
    @GET("messages/chats")
    Call<List<User>> getChatPartners();
    
    @GET("messages/{id}")
    Call<List<Message>> getMessagesByUserId(@Path("id") String userId);
    
    @POST("messages/send/{id}")
    Call<Message> sendMessage(@Path("id") String receiverId, @Body SendMessageRequest request);
}
