package com.talksy.android.data.repository;

import android.content.Context;

import com.talksy.android.data.local.TalksyDatabase;
import com.talksy.android.data.local.UserDao;
import com.talksy.android.data.local.UserEntity;
import com.talksy.android.data.model.User;
import com.talksy.android.data.remote.ApiClient;
import com.talksy.android.data.remote.AuthApiService;
import com.talksy.android.utils.TokenManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    
    private AuthApiService authApiService;
    private UserDao userDao;
    private ExecutorService executor;
    
    public AuthRepository(Context context) {
        this.authApiService = ApiClient.getAuthApiService(context);
        this.userDao = TalksyDatabase.getDatabase(context).userDao();
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String error);
    }
    
    public void login(String email, String password, AuthCallback callback) {
        Call<User> call = authApiService.login(new com.talksy.android.data.model.LoginRequest(email, password));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    // Save user data locally
                    executor.execute(() -> {
                        UserEntity userEntity = convertToEntity(user);
                        userDao.insertUser(userEntity);
                    });
                    callback.onSuccess(user);
                } else {
                    callback.onError("Login failed");
                }
            }
            
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    public void signup(String fullName, String email, String password, AuthCallback callback) {
        Call<User> call = authApiService.signup(new com.talksy.android.data.model.SignupRequest(fullName, email, password));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    // Save user data locally
                    executor.execute(() -> {
                        UserEntity userEntity = convertToEntity(user);
                        userDao.insertUser(userEntity);
                    });
                    callback.onSuccess(user);
                } else {
                    callback.onError("Signup failed");
                }
            }
            
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    public void logout(AuthCallback callback) {
        Call<Void> call = authApiService.logout();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callback.onSuccess(null);
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    public void checkAuth(AuthCallback callback) {
        Call<User> call = authApiService.checkAuth();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Authentication failed");
                }
            }
            
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
    
    private UserEntity convertToEntity(User user) {
        UserEntity entity = new UserEntity(user.get_id(), user.getEmail(), user.getFullName(), user.getProfilePic());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }
}
