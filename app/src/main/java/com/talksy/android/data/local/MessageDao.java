package com.talksy.android.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageDao {
    
    @Query("SELECT * FROM messages WHERE (senderId = :userId1 AND receiverId = :userId2) OR (senderId = :userId2 AND receiverId = :userId1) ORDER BY createdAt ASC")
    List<MessageEntity> getMessagesBetweenUsers(String userId1, String userId2);
    
    @Query("SELECT * FROM messages WHERE senderId = :userId OR receiverId = :userId ORDER BY createdAt DESC")
    List<MessageEntity> getAllUserMessages(String userId);
    
    @Query("SELECT * FROM messages WHERE id = :messageId")
    MessageEntity getMessageById(String messageId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(MessageEntity message);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(List<MessageEntity> messages);
    
    @Update
    void updateMessage(MessageEntity message);
    
    @Query("UPDATE messages SET isDelivered = :isDelivered WHERE id = :messageId")
    void updateMessageDeliveryStatus(String messageId, boolean isDelivered);
    
    @Query("UPDATE messages SET isRead = :isRead WHERE id = :messageId")
    void updateMessageReadStatus(String messageId, boolean isRead);
    
    @Delete
    void deleteMessage(MessageEntity message);
    
    @Query("DELETE FROM messages WHERE senderId = :userId OR receiverId = :userId")
    void deleteUserMessages(String userId);
    
    @Query("DELETE FROM messages")
    void deleteAllMessages();
}
