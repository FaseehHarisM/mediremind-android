package com.example.mediremind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediremind.R;
import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.History;
import com.example.mediremind.models.Medicine;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class CaregiverActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextView tvPatient, tvMissedCount, tvLowStockCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver);
        
        db = new DatabaseHelper(this);
        
        tvPatient = findViewById(R.id.tvCaregiverPatient);
        tvMissedCount = findViewById(R.id.tvMissedCount);
        tvLowStockCount = findViewById(R.id.tvLowStockCount);
        
        SharedPreferences prefs = getSharedPreferences("MediRemindPrefs", MODE_PRIVATE);
        tvPatient.setText("Caring for: " + prefs.getString("user_name", "Patient"));
        
        MaterialButton btnHistory = findViewById(R.id.btnCaregiverHistory);
        MaterialButton btnManage = findViewById(R.id.btnCaregiverManage);
        
        btnHistory.setOnClickListener(v -> startActivity(new Intent(CaregiverActivity.this, HistoryActivity.class)));
        btnManage.setOnClickListener(v -> startActivity(new Intent(CaregiverActivity.this, MainActivity.class)));
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        calculateWarnings();
    }
    
    private void calculateWarnings() {
        int missed = 0;
        List<History> historyList = db.getAllHistory();
        for (History h : historyList) {
            if ("MISSED".equals(h.getStatus())) missed++;
        }
        tvMissedCount.setText(String.valueOf(missed));
        
        int lowStock = 0;
        List<Medicine> meds = db.getAllMedicines();
        for (Medicine m : meds) {
            if (m.getQuantity() <= m.getLowStockLevel()) lowStock++;
        }
        tvLowStockCount.setText(String.valueOf(lowStock));
    }
}
