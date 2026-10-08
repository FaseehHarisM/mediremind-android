package com.example.mediremind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediremind.R;
import com.example.mediremind.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        SwitchMaterial switchNotif = findViewById(R.id.switchNotifications);
        SwitchMaterial switchSound = findViewById(R.id.switchSound);
        SwitchMaterial switchVib = findViewById(R.id.switchVibration);
        
        MaterialButton btnManageContacts = findViewById(R.id.btnManageContacts);
        MaterialButton btnClearHistory = findViewById(R.id.btnClearHistory);
        MaterialButton btnResetApp = findViewById(R.id.btnResetApp);
        
        SharedPreferences prefs = getSharedPreferences("MediRemindPrefs", MODE_PRIVATE);
        switchNotif.setChecked(prefs.getBoolean("notifications_enabled", true));
        switchSound.setChecked(prefs.getBoolean("sound_enabled", true));
        switchVib.setChecked(prefs.getBoolean("vibration_enabled", true));
        
        switchNotif.setOnCheckedChangeListener((v, isChecked) -> prefs.edit().putBoolean("notifications_enabled", isChecked).apply());
        switchSound.setOnCheckedChangeListener((v, isChecked) -> prefs.edit().putBoolean("sound_enabled", isChecked).apply());
        switchVib.setOnCheckedChangeListener((v, isChecked) -> prefs.edit().putBoolean("vibration_enabled", isChecked).apply());
        
        btnManageContacts.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, ContactActivity.class));
        });
        
        btnClearHistory.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Clear History")
                .setMessage("Are you sure you want to delete all medication history?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    DatabaseHelper db = new DatabaseHelper(this);
                    db.getWritableDatabase().execSQL("DELETE FROM history");
                    Toast.makeText(this, "History Cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
        });
        
        btnResetApp.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle("Reset App")
                .setMessage("This will delete ALL medicines, history, and settings. Are you sure?")
                .setPositiveButton("Reset", (dialog, which) -> {
                    DatabaseHelper db = new DatabaseHelper(this);
                    db.getWritableDatabase().execSQL("DELETE FROM medicines");
                    db.getWritableDatabase().execSQL("DELETE FROM history");
                    prefs.edit().clear().apply();
                    Toast.makeText(this, "App Reset", Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
    }
}
