package com.example.yourchat;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourchat.model.UserModel;
import com.example.yourchat.utils.AndroidUtil;
import com.example.yourchat.utils.FirebaseUtil;
import com.google.firebase.analytics.FirebaseAnalytics;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, new Bundle());

        if (getIntent().getExtras() != null && getIntent().hasExtra("userId")) {
            //from notification
            String userId = getIntent().getExtras().getString("userId");
            Log.d("SPLASH", "userid: " +userId);
            FirebaseUtil.allUserCollectionReference().document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            UserModel model = task.getResult().toObject(UserModel.class);

                            Intent mainIntent = new Intent(this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(mainIntent);

                            Intent intent = new Intent(this, ChatActivity.class);
                            AndroidUtil.passUserModelAsIntent(intent, model);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });


        } else {
            new Handler().postDelayed(() -> {
                if (FirebaseUtil.isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginPhoneNumberActivity.class));
                }
                finish();
            }, 1000);
        }
    }
}