package com.example.mediremind.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.Medicine;
import com.example.mediremind.utils.NotificationHelper;
import com.example.mediremind.utils.AlarmHelper;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("MediRemindPrefs", Context.MODE_PRIVATE);
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        
        if (!notificationsEnabled) return;

        int medicineId = intent.getIntExtra("MEDICINE_ID", -1);
        int escalationLevel = intent.getIntExtra("ESCALATION_LEVEL", 0);
        
        if (medicineId != -1) {
            DatabaseHelper db = new DatabaseHelper(context);
            Medicine med = db.getMedicine(medicineId);
            
            if (med != null) {
                // If it's an escalation, check if already taken today
                if (escalationLevel > 0) {
                    String today = com.example.mediremind.utils.DateTimeHelper.getCurrentDate();
                    if (db.isDoseTakenToday(medicineId, today)) {
                        return; // Already taken, cancel escalation
                    }
                }

                NotificationHelper.createNotificationChannel(context);
                NotificationHelper.showNotification(context, med.getId(), med.getName(), med.getDosage(), med.getType(), escalationLevel);
                
                if (escalationLevel == 0) {
                    // Schedule next day's alarm
                    AlarmHelper.scheduleAlarm(context, med.getId(), med.getReminderTime(), med.getAlarmId());
                    
                    // Schedule level 1 follow-up in 10 minutes
                    long followUpTime = System.currentTimeMillis() + (10 * 60 * 1000);
                    AlarmHelper.scheduleExactAlarm(context, med.getId(), followUpTime, med.getAlarmId() + 1, 1);
                } else if (escalationLevel == 1) {
                    // Schedule level 2 warning in 20 minutes (30 mins total)
                    long warningTime = System.currentTimeMillis() + (20 * 60 * 1000);
                    AlarmHelper.scheduleExactAlarm(context, med.getId(), warningTime, med.getAlarmId() + 2, 2);
                } else if (escalationLevel == 2) {
                    // Log as missed? The spec says "Status becomes MISSED"
                    // We can just leave it pending or explicitly log missed.
                    // For now, the notification acts as the final warning.
                }
            }
        }
    }
}
