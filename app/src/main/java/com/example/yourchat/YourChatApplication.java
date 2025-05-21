package com.example.yourchat;
// import the basic Android Application class
import android.app.Application;
// import Room Database
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
// import of own Room Database
import com.example.yourchat.db.AppDatabase;
// import Firebase Analytics library
import com.google.firebase.analytics.FirebaseAnalytics;

// Main Class of YourChat app which extends Application
public class YourChatApplication extends Application {
    // instance for Firebase Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    // instance for Room Database
    private AppDatabase db;

    // method which is called when the application is started
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialization of Firebase Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
       // creating Room database "chat-database"
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "chat-database")
                .allowMainThreadQueries().build();
    }
    // method to access Firebase Analytics
    public FirebaseAnalytics getFirebaseAnalytics(){
        return mFirebaseAnalytics;
    }
    // method to access Room database
    public AppDatabase getRoomDatabase(){
        return db;
    }
}