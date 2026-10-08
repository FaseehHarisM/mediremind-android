package com.example.mediremind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediremind.R;
import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.History;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextView tvPatientName, tvAdherence, tvMedicineCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        
        db = new DatabaseHelper(this);
        
        tvPatientName = findViewById(R.id.tvDoctorPatientName);
        tvAdherence = findViewById(R.id.tvAdherence);
        tvMedicineCount = findViewById(R.id.tvMedicineCount);
        
        SharedPreferences prefs = getSharedPreferences("MediRemindPrefs", MODE_PRIVATE);
        String patientName = prefs.getString("user_name", "Patient");
        tvPatientName.setText("Patient: " + patientName);
        
        MaterialButton btnAddMed = findViewById(R.id.btnDoctorAddMed);
        MaterialButton btnViewMeds = findViewById(R.id.btnDoctorViewMeds);
        MaterialButton btnHistory = findViewById(R.id.btnDoctorHistory);
        MaterialButton btnReport = findViewById(R.id.btnDoctorReport);
        
        btnAddMed.setOnClickListener(v -> startActivity(new Intent(DoctorActivity.this, AddMedicineActivity.class)));
        btnViewMeds.setOnClickListener(v -> startActivity(new Intent(DoctorActivity.this, MainActivity.class))); 
        btnHistory.setOnClickListener(v -> startActivity(new Intent(DoctorActivity.this, HistoryActivity.class)));
        btnReport.setOnClickListener(v -> startActivity(new Intent(DoctorActivity.this, ReportActivity.class)));
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        calculateStats();
    }
    
    private void calculateStats() {
        int medCount = db.getAllMedicines().size();
        tvMedicineCount.setText(String.valueOf(medCount));
        
        List<History> history = db.getAllHistory();
        if (history.isEmpty()) {
            tvAdherence.setText("N/A");
        } else {
            int taken = 0;
            for (History h : history) {
                if ("TAKEN".equals(h.getStatus())) taken++;
            }
            int adherence = (int) (((float) taken / history.size()) * 100);
            tvAdherence.setText(adherence + "%");
        }
    }
}
