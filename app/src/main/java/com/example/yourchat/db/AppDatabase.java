package com.example.yourchat.db;
import androidx.room.Database;
import androidx.room.RoomDatabase;

// Annotation showing this class is Room Database
// entities = {Draft.class} contains only one table: Draft
// version = 1 indicates the Database version
@Database(entities = {Draft.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DraftDao draftDao();
}
