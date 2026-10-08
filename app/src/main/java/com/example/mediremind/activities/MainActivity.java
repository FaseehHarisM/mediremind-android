package com.example.mediremind.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mediremind.R;
import com.example.mediremind.adapters.MedicineAdapter;
import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.History;
import com.example.mediremind.models.Medicine;
import com.example.mediremind.utils.DateTimeHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvMedicines;
    private MedicineAdapter adapter;
    private DatabaseHelper db;
    private TextView tvGreeting, tvProgressDetail;
    private ProgressBar progressDaily;
    private List<Medicine> currentList;
    
    private static final int PERMISSION_REQUEST_CODE = 1001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        db = new DatabaseHelper(this);
        rvMedicines = findViewById(R.id.rvMedicines);
        rvMedicines.setLayoutManager(new LinearLayoutManager(this));
        
        tvGreeting = findViewById(R.id.tvGreeting);
        tvProgressDetail = findViewById(R.id.tvProgressDetail);
        progressDaily = findViewById(R.id.progressDaily);
        
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddMedicineActivity.class)));
        
        setupBottomNav();
        setupSwipeGestures();
        updateGreeting();
        checkPermissions();
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_history) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return true;
        });
    }

    private void setupSwipeGestures() {
        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            private final ColorDrawable background = new ColorDrawable(Color.parseColor("#4CAF50")); // Green
            private final Paint paint = new Paint();

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Medicine med = currentList.get(position);
                
                if (direction == ItemTouchHelper.RIGHT) {
                    if (!med.getStatus().equals("TAKEN")) {
                        // Mark as taken
                        History h = new History();
                        h.setMedicineId(med.getId());
                        h.setMedicineName(med.getName());
                        h.setDosage(med.getDosage() + " " + med.getType());
                        h.setScheduledTime(med.getReminderTime());
                        h.setTakenDate(DateTimeHelper.getCurrentDate());
                        h.setTakenTime(DateTimeHelper.getCurrentTime());
                        h.setStatus("TAKEN");
                        db.addHistory(h);
                        db.decreaseStock(med.getId()); // NEW: Decrease stock
                        
                        Toast.makeText(MainActivity.this, "Marked as Taken!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Already taken today", Toast.LENGTH_SHORT).show();
                    }
                    // Reload to update UI
                    loadMedicines();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                if (dX > 0) { // Swiping to the right
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                    background.draw(c);
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(40);
                    c.drawText("TAKE", itemView.getLeft() + 50, itemView.getTop() + (itemView.getHeight() / 2f) + 15, paint);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(touchHelperCallback).attachToRecyclerView(rvMedicines);
    }
    
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedicines();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
    
    private void updateGreeting() {
        SharedPreferences prefs = getSharedPreferences("MediRemindPrefs", MODE_PRIVATE);
        String name = prefs.getString("user_name", "User");
        
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greeting = "Good Morning";
        if(timeOfDay >= 12 && timeOfDay < 16){
            greeting = "Good Afternoon";
        } else if(timeOfDay >= 16 && timeOfDay < 21){
            greeting = "Good Evening";
        } else if(timeOfDay >= 21){
            greeting = "Good Night";
        }
        
        tvGreeting.setText(greeting + ", " + name);
    }
    
    private void loadMedicines() {
        List<Medicine> allMeds = db.getAllMedicines();
        currentList = new java.util.ArrayList<>();
        String today = DateTimeHelper.getCurrentDate();
        
        for (Medicine med : allMeds) {
            boolean include = false;
            if (med.isTodayOnly()) {
                if (today.equals(med.getStartDate())) include = true;
            } else {
                if (med.getStartDate() != null && med.getEndDate() != null) {
                    if (today.compareTo(med.getStartDate()) >= 0 && today.compareTo(med.getEndDate()) <= 0) {
                        include = true;
                    }
                } else {
                    include = true; // Fallback if no dates set
                }
            }
            if (include) {
                currentList.add(med);
            }
        }
        
        int total = currentList.size();
        int takenCount = 0;
        
        for (Medicine med : currentList) {
            boolean taken = db.isDoseTakenToday(med.getId(), today);
            if (taken) {
                med.setStatus("TAKEN");
                takenCount++;
            } else {
                med.setStatus("UPCOMING");
            }
        }
        
        if (total > 0) {
            int progressPercent = (int) (((float) takenCount / total) * 100);
            progressDaily.setProgress(progressPercent);
            tvProgressDetail.setText(takenCount + " of " + total + " taken today");
        } else {
            progressDaily.setProgress(0);
            tvProgressDetail.setText("No medicines scheduled");
        }
        
        currentList.sort((m1, m2) -> {
            if (m1.getStatus().equals(m2.getStatus())) {
                return m1.getReminderTime().compareTo(m2.getReminderTime());
            }
            return m1.getStatus().equals("UPCOMING") ? -1 : 1;
        });
        
        if (adapter == null) {
            adapter = new MedicineAdapter(this, currentList);
            rvMedicines.setAdapter(adapter);
        } else {
            adapter.updateList(currentList);
        }
    }
}
