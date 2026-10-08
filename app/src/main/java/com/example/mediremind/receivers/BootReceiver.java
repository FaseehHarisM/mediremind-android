package com.example.mediremind.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.Medicine;
import com.example.mediremind.utils.AlarmHelper;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            DatabaseHelper db = new DatabaseHelper(context);
            List<Medicine> medicines = db.getAllMedicines();
            for (Medicine med : medicines) {
                AlarmHelper.scheduleAlarm(context, med.getId(), med.getReminderTime(), med.getAlarmId());
            }
        }
    }
}
