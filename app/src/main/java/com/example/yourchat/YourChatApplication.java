package com.example.yourchat;
import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.yourchat.db.AppDatabase;
import com.google.firebase.analytics.FirebaseAnalytics;

public class YourChatApplication extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    private AppDatabase db;


    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "chat-database")
                .allowMainThreadQueries().build();
    }

    public FirebaseAnalytics getFirebaseAnalytics(){
        return mFirebaseAnalytics;
    }

    public AppDatabase getRoomDatabase(){
        return db;
    }
}