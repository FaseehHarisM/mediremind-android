package com.example.mediremind.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.example.mediremind.receivers.ReminderReceiver;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmHelper {
    public static void scheduleAlarm(Context context, int medicineId, String timeString, int alarmId) {
        scheduleAlarmWithLevel(context, medicineId, timeString, alarmId, 0);
    }

    public static void scheduleAlarmWithLevel(Context context, int medicineId, String timeString, int alarmId, int escalationLevel) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = sdf.parse(timeString);
            Calendar timeToSet = Calendar.getInstance();
            Calendar parsedTime = Calendar.getInstance();
            parsedTime.setTime(date);
            
            timeToSet.set(Calendar.HOUR_OF_DAY, parsedTime.get(Calendar.HOUR_OF_DAY));
            timeToSet.set(Calendar.MINUTE, parsedTime.get(Calendar.MINUTE));
            timeToSet.set(Calendar.SECOND, 0);

            if (timeToSet.before(Calendar.getInstance())) {
                timeToSet.add(Calendar.DATE, 1);
            }

            scheduleExactAlarm(context, medicineId, timeToSet.getTimeInMillis(), alarmId, escalationLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scheduleExactAlarm(Context context, int medicineId, long timeInMillis, int alarmId, int escalationLevel) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("MEDICINE_ID", medicineId);
        intent.putExtra("ESCALATION_LEVEL", escalationLevel);
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            }
        }
    }

    public static void cancelAlarm(Context context, int alarmId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
