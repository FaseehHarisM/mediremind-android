package com.example.mediremind.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediremind.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SetupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        
        TextInputEditText etName = findViewById(R.id.etSetupName);
        TextInputEditText etAge = findViewById(R.id.etSetupAge);
        RadioGroup rgMode = findViewById(R.id.rgMode);
        MaterialButton btnContinue = findViewById(R.id.btnSetupContinue);
        
        btnContinue.setOnClickListener(v -> {
            String name = etName.getText() != null ? etName.getText().toString().trim() : "";
            String age = etAge.getText() != null ? etAge.getText().toString().trim() : "";
            
            if(name.isEmpty()) {
                name = "User";
            }
            
            String selectedMode = "STANDARD";
            int checkedId = rgMode.getCheckedRadioButtonId();
            if (checkedId == R.id.rbElderly) {
                selectedMode = "ELDER";
            } else if (checkedId == R.id.rbCaregiver) {
                selectedMode = "CAREGIVER";
            } else if (checkedId == R.id.rbDoctor) {
                selectedMode = "DOCTOR";
            }
            
            SharedPreferences prefs = getSharedPreferences("MediRemindPrefs", MODE_PRIVATE);
            prefs.edit()
                .putString("user_name", name)
                .putString("user_age", age)
                .putString("app_mode", selectedMode)
                .putBoolean("first_launch_done", true)
                .apply();
                
            Toast.makeText(this, "Welcome to MediRemind!", Toast.LENGTH_SHORT).show();
            
            if ("ELDER".equals(selectedMode)) {
                startActivity(new Intent(SetupActivity.this, ElderActivity.class));
            } else if ("DOCTOR".equals(selectedMode)) {
                startActivity(new Intent(SetupActivity.this, DoctorActivity.class));
            } else if ("CAREGIVER".equals(selectedMode)) {
                startActivity(new Intent(SetupActivity.this, CaregiverActivity.class));
            } else {
                startActivity(new Intent(SetupActivity.this, MainActivity.class));
            }
            finish();
        });
    }
}
