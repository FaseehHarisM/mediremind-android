package com.example.mediremind.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.History;
import com.example.mediremind.models.Medicine;
import com.example.mediremind.utils.DateTimeHelper;

public class NotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("ACTION_TAKE_MEDICINE".equals(intent.getAction())) {
            int medicineId = intent.getIntExtra("MEDICINE_ID", -1);
            if (medicineId != -1) {
                DatabaseHelper db = new DatabaseHelper(context);
                Medicine med = db.getMedicine(medicineId);
                
                if (med != null) {
                    String today = DateTimeHelper.getCurrentDate();
                    
                    // Only log if not already taken today
                    if (!db.isDoseTakenToday(medicineId, today)) {
                        History h = new History();
                        h.setMedicineId(med.getId());
                        h.setMedicineName(med.getName());
                        h.setDosage(med.getDosage() + " " + med.getType());
                        h.setScheduledTime(med.getReminderTime());
                        h.setTakenDate(today);
                        h.setTakenTime(DateTimeHelper.getCurrentTime());
                        h.setStatus("TAKEN");
                        db.addHistory(h);
                        db.decreaseStock(medicineId);
                        
                        Toast.makeText(context, "Logged: " + med.getName() + " Taken!", Toast.LENGTH_SHORT).show();
                    }
                }
                
                // Dismiss the notification
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.cancel(medicineId);
                }
            }
        } else if ("ACTION_SNOOZE_MEDICINE".equals(intent.getAction())) {
            int medicineId = intent.getIntExtra("MEDICINE_ID", -1);
            if (medicineId != -1) {
                DatabaseHelper db = new DatabaseHelper(context);
                Medicine med = db.getMedicine(medicineId);
                if (med != null) {
                    // Snooze for 10 minutes
                    long snoozeTime = System.currentTimeMillis() + (10 * 60 * 1000);
                    // Schedule as level 1 (so next is final warning)
                    com.example.mediremind.utils.AlarmHelper.scheduleExactAlarm(context, med.getId(), snoozeTime, med.getAlarmId() + 5, 1);
                    Toast.makeText(context, "Snoozed for 10 minutes", Toast.LENGTH_SHORT).show();
                    
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (manager != null) {
                        manager.cancel(medicineId);
                    }
                }
            }
        }
    }
}
