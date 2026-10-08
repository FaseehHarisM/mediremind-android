package com.example.mediremind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediremind.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("MediRemindPrefs", MODE_PRIVATE);
            boolean isFirstLaunch = !prefs.getBoolean("first_launch_done", false);
            
            if (isFirstLaunch) {
                startActivity(new Intent(SplashActivity.this, SetupActivity.class));
            } else {
                String mode = prefs.getString("app_mode", "STANDARD");
                if ("ELDER".equals(mode)) {
                    startActivity(new Intent(SplashActivity.this, ElderActivity.class));
                } else if ("DOCTOR".equals(mode)) {
                    startActivity(new Intent(SplashActivity.this, DoctorActivity.class));
                } else if ("CAREGIVER".equals(mode)) {
                    startActivity(new Intent(SplashActivity.this, CaregiverActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }
            finish();
        }, 1500);
    }
}
