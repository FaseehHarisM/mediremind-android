package com.example.mediremind.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediremind.R;
import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.Medicine;
import com.example.mediremind.utils.AlarmHelper;
import com.example.mediremind.utils.DateTimeHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Arrays;
import java.util.Calendar;

public class AddMedicineActivity extends AppCompatActivity {
    private TextInputEditText etName, etDosage, etNotes, etQuantity, etLowStock;
    private Spinner spinnerType, spinnerMeal;
    private MaterialButton tvSelectTime, btnStartDate, btnEndDate, btnSave;
    private SwitchMaterial switchTodayOnly;
    private String selectedTime = "";
    private String startDate = "";
    private String endDate = "";
    private int medicineId = -1;
    private Medicine editMedicine = null;
    private DatabaseHelper db;
    private java.util.List<String> selectedTimesList = new java.util.ArrayList<>();
    private String[] types = {"Tablet", "Capsule", "Syrup", "Drops", "Injection", "Other"};
    private String[] meals = {"Before Breakfast", "After Breakfast", "With Breakfast", "Before Lunch", "After Lunch", "With Lunch", "Before Dinner", "After Dinner", "With Dinner", "Before Bed", "Anytime"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        
        db = new DatabaseHelper(this);
        
        etName = findViewById(R.id.etMedicineName);
        etDosage = findViewById(R.id.etDosage);
        etNotes = findViewById(R.id.etNotes);
        etQuantity = findViewById(R.id.etQuantity);
        etLowStock = findViewById(R.id.etLowStock);
        spinnerType = findViewById(R.id.spinnerType);
        spinnerMeal = findViewById(R.id.spinnerMeal);
        tvSelectTime = findViewById(R.id.tvSelectTime);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        switchTodayOnly = findViewById(R.id.switchTodayOnly);
        btnSave = findViewById(R.id.btnSave);
        
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(typeAdapter);
        
        ArrayAdapter<String> mealAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, meals);
        spinnerMeal.setAdapter(mealAdapter);
        
        startDate = DateTimeHelper.getCurrentDate();
        endDate = DateTimeHelper.getCurrentDate(); // Default same day
        btnStartDate.setText(startDate);
        btnEndDate.setText(endDate);
        
        tvSelectTime.setText("ADD REMINDER TIME");
        tvSelectTime.setOnClickListener(v -> showTimePicker());
        btnStartDate.setOnClickListener(v -> showDatePicker(true));
        btnEndDate.setOnClickListener(v -> showDatePicker(false));
        
        switchTodayOnly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnEndDate.setEnabled(!isChecked);
        });
        
        btnSave.setOnClickListener(v -> saveMedicine());
        
        medicineId = getIntent().getIntExtra("MEDICINE_ID", -1);
        if (medicineId != -1) {
            editMedicine = db.getMedicine(medicineId);
            if(editMedicine != null) {
                populateData();
            }
        }
    }
    
    private void populateData() {
        etName.setText(editMedicine.getName());
        etDosage.setText(editMedicine.getDosage());
        etNotes.setText(editMedicine.getNotes());
        etQuantity.setText(String.valueOf(editMedicine.getQuantity()));
        etLowStock.setText(String.valueOf(editMedicine.getLowStockLevel()));
        
        selectedTime = editMedicine.getReminderTime();
        selectedTimesList.add(selectedTime);
        tvSelectTime.setText("Times: " + selectedTime);
        
        int typeIndex = Arrays.asList(types).indexOf(editMedicine.getType());
        if(typeIndex >= 0) spinnerType.setSelection(typeIndex);
        
        int mealIndex = Arrays.asList(meals).indexOf(editMedicine.getMealRelation());
        if(mealIndex >= 0) spinnerMeal.setSelection(mealIndex);
        
        startDate = editMedicine.getStartDate() != null ? editMedicine.getStartDate() : DateTimeHelper.getCurrentDate();
        endDate = editMedicine.getEndDate() != null ? editMedicine.getEndDate() : DateTimeHelper.getCurrentDate();
        btnStartDate.setText(startDate);
        btnEndDate.setText(endDate);
        
        switchTodayOnly.setChecked(editMedicine.isTodayOnly());
    }
    
    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String time = DateTimeHelper.getFormattedTime(hourOfDay, minute);
            selectedTimesList.add(time);
            tvSelectTime.setText("Times: " + String.join(", ", selectedTimesList));
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
    }
    
    private void showDatePicker(boolean isStart) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            if (isStart) {
                startDate = formattedDate;
                btnStartDate.setText(startDate);
            } else {
                endDate = formattedDate;
                btnEndDate.setText(endDate);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
    
    private void saveMedicine() {
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String dosage = etDosage.getText() != null ? etDosage.getText().toString().trim() : "";
        String qtyStr = etQuantity.getText() != null ? etQuantity.getText().toString().trim() : "";
        String lowStr = etLowStock.getText() != null ? etLowStock.getText().toString().trim() : "";
        
        if (name.isEmpty() || dosage.isEmpty() || selectedTimesList.isEmpty()) {
            Toast.makeText(this, "Please fill required fields and select at least one time", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int qty = qtyStr.isEmpty() ? 0 : Integer.parseInt(qtyStr);
        int lowStock = lowStr.isEmpty() ? 5 : Integer.parseInt(lowStr);
        
        if (editMedicine == null) {
            for (String time : selectedTimesList) {
                Medicine med = new Medicine();
                med.setName(name);
                med.setDosage(dosage);
                med.setType(spinnerType.getSelectedItem().toString());
                med.setMealRelation(spinnerMeal.getSelectedItem().toString());
                med.setReminderTime(time);
                med.setStartDate(startDate);
                med.setEndDate(endDate);
                med.setTodayOnly(switchTodayOnly.isChecked());
                med.setFrequency("Every day");
                med.setNotes(etNotes.getText() != null ? etNotes.getText().toString().trim() : "");
                med.setStatus("UPCOMING");
                med.setCreatedAt(DateTimeHelper.getCurrentDate());
                med.setQuantity(qty);
                med.setLowStockLevel(lowStock);
                
                int alarmId = (int) System.currentTimeMillis() + (int)(Math.random()*10000);
                med.setAlarmId(alarmId);
                
                long id = db.addMedicine(med);
                if (id != -1) {
                    AlarmHelper.scheduleAlarm(this, (int) id, time, alarmId);
                }
            }
            Toast.makeText(this, "Medicines Scheduled!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Edit mode logic - assuming they just change the one they clicked
            editMedicine.setName(name);
            editMedicine.setDosage(dosage);
            editMedicine.setType(spinnerType.getSelectedItem().toString());
            editMedicine.setMealRelation(spinnerMeal.getSelectedItem().toString());
            editMedicine.setReminderTime(selectedTimesList.get(0)); // Only take first for edit
            editMedicine.setStartDate(startDate);
            editMedicine.setEndDate(endDate);
            editMedicine.setTodayOnly(switchTodayOnly.isChecked());
            editMedicine.setNotes(etNotes.getText() != null ? etNotes.getText().toString().trim() : "");
            editMedicine.setQuantity(qty);
            editMedicine.setLowStockLevel(lowStock);
            
            AlarmHelper.cancelAlarm(this, editMedicine.getAlarmId());
            int newAlarmId = (int) System.currentTimeMillis();
            editMedicine.setAlarmId(newAlarmId);
            
            int rows = db.updateMedicine(editMedicine);
            if (rows > 0) {
                AlarmHelper.scheduleAlarm(this, editMedicine.getId(), selectedTimesList.get(0), newAlarmId);
                Toast.makeText(this, "Medicine Updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error updating", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
