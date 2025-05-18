package com.example.yourchat.db;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Draft.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DraftDao draftDao();
}
