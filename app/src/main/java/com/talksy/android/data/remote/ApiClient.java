package com.talksy.android.data.remote;

import android.content.Context;

import com.talksy.android.BuildConfig;
import com.talksy.android.utils.TokenManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    
    private static Retrofit retrofit;
    
    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            retrofit = createRetrofit(context);
        }
        return retrofit;
    }
    
    private static Retrofit createRetrofit(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        AuthInterceptor authInterceptor = new AuthInterceptor(context);
        
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    
    public static AuthApiService getAuthApiService(Context context) {
        return getRetrofit(context).create(AuthApiService.class);
    }
    
    public static MessageApiService getMessageApiService(Context context) {
        return getRetrofit(context).create(MessageApiService.class);
    }
}

class AuthInterceptor implements Interceptor {
    
    private Context context;
    
    public AuthInterceptor(Context context) {
        this.context = context;
    }
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = TokenManager.getToken(context);
        
        Request newRequest;
        if (token != null) {
            newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
        } else {
            newRequest = originalRequest;
        }
        
        return chain.proceed(newRequest);
    }
}
