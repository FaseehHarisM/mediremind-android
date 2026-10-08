package com.example.mediremind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediremind.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ContactActivity extends AppCompatActivity {
    private TextInputEditText etDoctorName, etDoctorPhone, etEmergencyName, etEmergencyPhone;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        
        etDoctorName = findViewById(R.id.etDoctorName);
        etDoctorPhone = findViewById(R.id.etDoctorPhone);
        etEmergencyName = findViewById(R.id.etEmergencyName);
        etEmergencyPhone = findViewById(R.id.etEmergencyPhone);
        
        MaterialButton btnCallDoctor = findViewById(R.id.btnCallDoctor);
        MaterialButton btnCallEmergency = findViewById(R.id.btnCallEmergency);
        MaterialButton btnSaveContacts = findViewById(R.id.btnSaveContacts);
        
        loadContacts();
        
        btnSaveContacts.setOnClickListener(v -> saveContacts());
        
        btnCallDoctor.setOnClickListener(v -> {
            String phone = etDoctorPhone.getText() != null ? etDoctorPhone.getText().toString().trim() : "";
            makePhoneCall(phone);
        });
        
        btnCallEmergency.setOnClickListener(v -> {
            String phone = etEmergencyPhone.getText() != null ? etEmergencyPhone.getText().toString().trim() : "";
            makePhoneCall(phone);
        });
    }
    
    private void loadContacts() {
        SharedPreferences prefs = getSharedPreferences("MediRemindPrefs", MODE_PRIVATE);
        etDoctorName.setText(prefs.getString("doctor_name", ""));
        etDoctorPhone.setText(prefs.getString("doctor_phone", ""));
        etEmergencyName.setText(prefs.getString("emergency_name", ""));
        etEmergencyPhone.setText(prefs.getString("emergency_phone", ""));
    }
    
    private void saveContacts() {
        SharedPreferences prefs = getSharedPreferences("MediRemindPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        editor.putString("doctor_name", etDoctorName.getText() != null ? etDoctorName.getText().toString().trim() : "");
        editor.putString("doctor_phone", etDoctorPhone.getText() != null ? etDoctorPhone.getText().toString().trim() : "");
        editor.putString("emergency_name", etEmergencyName.getText() != null ? etEmergencyName.getText().toString().trim() : "");
        editor.putString("emergency_phone", etEmergencyPhone.getText() != null ? etEmergencyPhone.getText().toString().trim() : "");
        editor.apply();
        
        Toast.makeText(this, "Contacts Saved Successfully", Toast.LENGTH_SHORT).show();
    }
    
    private void makePhoneCall(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
