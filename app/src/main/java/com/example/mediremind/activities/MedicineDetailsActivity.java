package com.example.mediremind.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediremind.R;
import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.History;
import com.example.mediremind.models.Medicine;
import com.example.mediremind.utils.AlarmHelper;
import com.example.mediremind.utils.DateTimeHelper;

public class MedicineDetailsActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private Medicine med;
    private int medId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);
        
        db = new DatabaseHelper(this);
        medId = getIntent().getIntExtra("MEDICINE_ID", -1);
        
        Button btnTaken = findViewById(R.id.btnMarkTaken);
        btnTaken.setOnClickListener(v -> markAsTaken());
        
        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(MedicineDetailsActivity.this, AddMedicineActivity.class);
            intent.putExtra("MEDICINE_ID", med.getId());
            startActivity(intent);
        });
        
        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> deleteMedicine());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (medId != -1) {
            med = db.getMedicine(medId);
            if(med != null) {
                populateUI();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }
    
    private void populateUI() {
        TextView tvDetailName = findViewById(R.id.tvDetailName);
        TextView tvDetailDosageType = findViewById(R.id.tvDetailDosageType);
        TextView tvDetailTime = findViewById(R.id.tvDetailTime);
        TextView tvDetailNotes = findViewById(R.id.tvDetailNotes);
        TextView tvDetailStatus = findViewById(R.id.tvDetailStatus);
        
        tvDetailName.setText(med.getName());
        tvDetailDosageType.setText(med.getDosage() + " • " + med.getType());
        tvDetailTime.setText("Reminder Time: " + med.getReminderTime());
        tvDetailNotes.setText("Notes: " + med.getNotes());
        
        boolean taken = db.isDoseTakenToday(med.getId(), DateTimeHelper.getCurrentDate());
        if(taken) {
            tvDetailStatus.setText("TAKEN");
        } else {
            tvDetailStatus.setText("UPCOMING");
        }
    }
    
    private void markAsTaken() {
        if(db.isDoseTakenToday(med.getId(), DateTimeHelper.getCurrentDate())) {
            Toast.makeText(this, "Already taken today", Toast.LENGTH_SHORT).show();
            return;
        }
        
        History h = new History();
        h.setMedicineId(med.getId());
        h.setMedicineName(med.getName());
        h.setDosage(med.getDosage() + " " + med.getType());
        h.setScheduledTime(med.getReminderTime());
        h.setTakenDate(DateTimeHelper.getCurrentDate());
        h.setTakenTime(DateTimeHelper.getCurrentTime());
        h.setStatus("TAKEN");
        
        db.addHistory(h);
        Toast.makeText(this, med.getName() + " marked as taken.", Toast.LENGTH_SHORT).show();
        finish();
    }
    
    private void deleteMedicine() {
        AlarmHelper.cancelAlarm(this, med.getAlarmId());
        db.deleteMedicine(med.getId());
        Toast.makeText(this, "Medicine deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
