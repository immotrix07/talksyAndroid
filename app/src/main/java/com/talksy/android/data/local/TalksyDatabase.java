package com.talksy.android.data.local;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(
    entities = {UserEntity.class, MessageEntity.class},
    version = 1,
    exportSchema = false
)
public abstract class TalksyDatabase extends RoomDatabase {
    
    public abstract UserDao userDao();
    public abstract MessageDao messageDao();
    
    private static volatile TalksyDatabase INSTANCE;
    
    public static TalksyDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (TalksyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TalksyDatabase.class,
                            "talksy_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
