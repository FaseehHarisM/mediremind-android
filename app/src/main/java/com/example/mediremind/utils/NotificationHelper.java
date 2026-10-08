package com.example.mediremind.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.mediremind.R;
import com.example.mediremind.activities.MedicineDetailsActivity;
import com.example.mediremind.receivers.NotificationActionReceiver;

public class NotificationHelper {
    private static final String CHANNEL_ID = "mediremind_channel";
    private static final String CHANNEL_NAME = "Medicine Reminders";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for medicine reminders");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void showNotification(Context context, int medicineId, String name, String dosage, String type, int escalationLevel) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        Intent intent = new Intent(context, MedicineDetailsActivity.class);
        intent.putExtra("MEDICINE_ID", medicineId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                medicineId, 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Action Intent for "Mark as Taken" button
        Intent takeIntent = new Intent(context, NotificationActionReceiver.class);
        takeIntent.setAction("ACTION_TAKE_MEDICINE");
        takeIntent.putExtra("MEDICINE_ID", medicineId);
        PendingIntent takePendingIntent = PendingIntent.getBroadcast(
                context,
                medicineId + 1000,
                takeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Action Intent for "Snooze" button
        Intent snoozeIntent = new Intent(context, NotificationActionReceiver.class);
        snoozeIntent.setAction("ACTION_SNOOZE_MEDICINE");
        snoozeIntent.putExtra("MEDICINE_ID", medicineId);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(
                context,
                medicineId + 2000,
                snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String title = "Medicine Reminder \u23F0";
        String content = "It's time to take " + name + " (" + dosage + " " + type + ")";
        
        if (escalationLevel == 1) {
            title = "Reminder: " + name + " is pending";
            content = "Please take your medicine: " + name;
        } else if (escalationLevel >= 2) {
            title = "\u26A0\uFE0F MEDICINE STILL PENDING";
            content = "You have not yet taken " + name + ". Please check.";
        }

        SharedPreferences prefs = context.getSharedPreferences("MediRemindPrefs", Context.MODE_PRIVATE);
        boolean soundEnabled = prefs.getBoolean("sound_enabled", true);
        boolean vibEnabled = prefs.getBoolean("vibration_enabled", true);
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(R.mipmap.ic_launcher, "Take Now \u2714\uFE0F", takePendingIntent);
                
        if (soundEnabled) {
            builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        }
        if (vibEnabled) {
            builder.setVibrate(new long[]{0, 500, 500, 500});
        }
                
        // Only add Snooze if it's not the final escalation
        if (escalationLevel < 2) {
            builder.addAction(R.mipmap.ic_launcher, "Snooze (10m) \u23F3", snoozePendingIntent);
        }

        if (manager != null) {
            manager.notify(medicineId, builder.build());
        }
    }
}
