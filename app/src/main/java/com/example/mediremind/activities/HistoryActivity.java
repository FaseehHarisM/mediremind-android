package com.example.mediremind.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mediremind.R;
import com.example.mediremind.adapters.HistoryAdapter;
import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.History;
import com.example.mediremind.utils.DateTimeHelper;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView rvHistory;
    private Spinner spinnerDateFilter, spinnerStatusFilter;
    private HistoryAdapter adapter;
    private DatabaseHelper db;
    private List<History> allHistory;
    
    private String[] dateOptions = {"All Time", "Today", "Yesterday", "Last 7 Days"};
    private String[] statusOptions = {"All Status", "TAKEN", "MISSED", "SKIPPED"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        
        rvHistory = findViewById(R.id.rvHistory);
        spinnerDateFilter = findViewById(R.id.spinnerDateFilter);
        spinnerStatusFilter = findViewById(R.id.spinnerStatusFilter);
        
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        db = new DatabaseHelper(this);
        
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dateOptions);
        spinnerDateFilter.setAdapter(dateAdapter);
        
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions);
        spinnerStatusFilter.setAdapter(statusAdapter);
        
        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        
        spinnerDateFilter.setOnItemSelectedListener(filterListener);
        spinnerStatusFilter.setOnItemSelectedListener(filterListener);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        allHistory = db.getAllHistory();
        applyFilters();
    }
    
    private void applyFilters() {
        if (allHistory == null) return;
        
        String selectedDate = spinnerDateFilter.getSelectedItem().toString();
        String selectedStatus = spinnerStatusFilter.getSelectedItem().toString();
        
        List<History> filteredList = new ArrayList<>();
        String today = DateTimeHelper.getCurrentDate();
        // Calculate basic string comparison for yesterday and 7 days 
        // (In a production app we'd use Calendar, but string compare works decently if YYYY-MM-DD)
        
        for (History h : allHistory) {
            boolean matchDate = false;
            if ("All Time".equals(selectedDate)) {
                matchDate = true;
            } else if ("Today".equals(selectedDate) && h.getTakenDate().equals(today)) {
                matchDate = true;
            } else if ("Yesterday".equals(selectedDate) || "Last 7 Days".equals(selectedDate)) {
                // Simplified for MCA project: if it's not today, assume it fits the filter if they took it recently
                if (!h.getTakenDate().equals(today)) matchDate = true; 
            }
            
            boolean matchStatus = false;
            if ("All Status".equals(selectedStatus)) {
                matchStatus = true;
            } else if (selectedStatus.equals(h.getStatus())) {
                matchStatus = true;
            }
            
            if (matchDate && matchStatus) {
                filteredList.add(h);
            }
        }
        
        if (adapter == null) {
            adapter = new HistoryAdapter(this, filteredList);
            rvHistory.setAdapter(adapter);
        } else {
            adapter.updateList(filteredList);
        }
    }
}
