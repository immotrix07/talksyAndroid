package com.talksy.android.data.network;

import com.talksy.android.BuildConfig;
import com.talksy.android.data.models.LoginResponse;
import com.talksy.android.data.models.SignupResponse;
import com.talksy.android.data.models.User;
import com.talksy.android.utils.TokenManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.io.IOException;
import java.util.List;
import android.content.Context;
import android.util.Log;
import com.talksy.android.data.models.Chat;
import com.talksy.android.data.models.Message;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static TokenManager tokenManager;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            tokenManager = new TokenManager(context);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
    .addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "Android");

            if (tokenManager.getToken() != null) {
                requestBuilder.header("Authorization", "Bearer " + tokenManager.getToken());
                Log.d("ApiClient", "Adding token: " + tokenManager.getToken());  // Add logging
            } else {
                Log.d("ApiClient", "No token available");  // Add logging
            }

            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    })
    .addInterceptor(loggingInterceptor)
    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public interface ApiService {
    @POST("auth/signup")
    Call<SignupResponse> signup(@Body User user);

    @POST("auth/login")
    Call<LoginResponse> login(@Body User user);

    @GET("chat")
    Call<List<Chat>> getChats();  // No header parameter needed since we're using interceptor

    @POST("chat")
    Call<Chat> createOrGetChat(@Body Chat chat);  // No header parameter needed

    @GET("messages/{chatId}")
    Call<List<Message>> getMessages(@Path("chatId") String chatId);  // No header parameter needed

    @POST("messages")
    Call<Message> sendMessage(@Body Message message);  // No header parameter needed
}
}
