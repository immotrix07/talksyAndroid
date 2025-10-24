package com.talksy.android.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    
    @Query("SELECT * FROM users")
    List<UserEntity> getAllUsers();
    
    @Query("SELECT * FROM users WHERE id = :userId")
    UserEntity getUserById(String userId);
    
    @Query("SELECT * FROM users WHERE id != :currentUserId")
    List<UserEntity> getContacts(String currentUserId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<UserEntity> users);
    
    @Update
    void updateUser(UserEntity user);
    
    @Query("UPDATE users SET isOnline = :isOnline WHERE id = :userId")
    void updateUserOnlineStatus(String userId, boolean isOnline);
    
    @Delete
    void deleteUser(UserEntity user);
    
    @Query("DELETE FROM users")
    void deleteAllUsers();
}
