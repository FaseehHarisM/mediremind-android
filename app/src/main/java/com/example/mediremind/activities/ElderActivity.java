package com.example.mediremind.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediremind.R;
import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.History;
import com.example.mediremind.models.Medicine;
import com.example.mediremind.utils.DateTimeHelper;
import java.util.Calendar;
import java.util.List;

public class ElderActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private Medicine nextMed = null;
    
    private TextView tvGreeting, tvMedName, tvDosage, tvTime, tvNotes;
    private Button btnTake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder);
        
        db = new DatabaseHelper(this);
        
        tvGreeting = findViewById(R.id.tvElderGreeting);
        tvMedName = findViewById(R.id.tvElderMedName);
        tvDosage = findViewById(R.id.tvElderDosage);
        tvTime = findViewById(R.id.tvElderTime);
        tvNotes = findViewById(R.id.tvElderNotes);
        btnTake = findViewById(R.id.btnElderTake);
        
        btnTake.setOnClickListener(v -> markAsTaken());
        
        updateGreeting();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadNextMedicine();
    }
    
    private void updateGreeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greeting = "GOOD MORNING";
        if(timeOfDay >= 12 && timeOfDay < 16){
            greeting = "GOOD AFTERNOON";
        } else if(timeOfDay >= 16 && timeOfDay < 21){
            greeting = "GOOD EVENING";
        } else if(timeOfDay >= 21){
            greeting = "GOOD NIGHT";
        }
        tvGreeting.setText(greeting);
    }
    
    private void loadNextMedicine() {
        List<Medicine> list = db.getAllMedicines();
        String today = DateTimeHelper.getCurrentDate();
        nextMed = null;
        
        for (Medicine med : list) {
            if (!db.isDoseTakenToday(med.getId(), today)) {
                if (nextMed == null) {
                    nextMed = med;
                } else if (med.getReminderTime().compareTo(nextMed.getReminderTime()) < 0) {
                    nextMed = med;
                }
            }
        }
        
        if (nextMed != null) {
            tvMedName.setText(nextMed.getName().toUpperCase());
            tvDosage.setText(nextMed.getDosage() + " " + nextMed.getType());
            tvTime.setText(nextMed.getReminderTime());
            tvNotes.setText(nextMed.getNotes().toUpperCase());
            btnTake.setVisibility(View.VISIBLE);
        } else {
            tvMedName.setText("ALL CAUGHT UP!");
            tvDosage.setText("Great job today.");
            tvTime.setText("");
            tvNotes.setText("");
            btnTake.setVisibility(View.GONE);
        }
    }
    
    private void markAsTaken() {
        if (nextMed != null) {
            History h = new History();
            h.setMedicineId(nextMed.getId());
            h.setMedicineName(nextMed.getName());
            h.setDosage(nextMed.getDosage() + " " + nextMed.getType());
            h.setScheduledTime(nextMed.getReminderTime());
            h.setTakenDate(DateTimeHelper.getCurrentDate());
            h.setTakenTime(DateTimeHelper.getCurrentTime());
            h.setStatus("TAKEN");
            db.addHistory(h);
            db.decreaseStock(nextMed.getId());
            
            Toast.makeText(this, "Logged! Great job.", Toast.LENGTH_LONG).show();
            loadNextMedicine(); // Reload to show next
        }
    }
}
