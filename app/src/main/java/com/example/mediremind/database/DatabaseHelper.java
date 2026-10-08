package com.example.mediremind.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.mediremind.models.Medicine;
import com.example.mediremind.models.History;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medicine.db";
    private static final int DATABASE_VERSION = 3; // Upgraded for PRD Core Fields

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMedicinesTable = "CREATE TABLE medicines (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "dosage TEXT NOT NULL, " +
                "type TEXT, " +
                "reminderTime TEXT NOT NULL, " +
                "frequency TEXT, " +
                "notes TEXT, " +
                "status TEXT, " +
                "createdAt TEXT, " +
                "alarmId INTEGER, " +
                "quantity INTEGER DEFAULT 0, " +
                "lowStockLevel INTEGER DEFAULT 5, " +
                "mealRelation TEXT, " +
                "startDate TEXT, " +
                "endDate TEXT, " +
                "todayOnly INTEGER DEFAULT 0)";
        db.execSQL(createMedicinesTable);

        String createHistoryTable = "CREATE TABLE history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "medicineId INTEGER, " +
                "medicineName TEXT, " +
                "dosage TEXT, " +
                "scheduledTime TEXT, " +
                "takenDate TEXT, " +
                "takenTime TEXT, " +
                "status TEXT)";
        db.execSQL(createHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
        db.execSQL("DROP TABLE IF EXISTS medicines");
        db.execSQL("DROP TABLE IF EXISTS history");
        onCreate(db);
    }

    public long addMedicine(Medicine med) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", med.getName());
        values.put("dosage", med.getDosage());
        values.put("type", med.getType());
        values.put("reminderTime", med.getReminderTime());
        values.put("frequency", med.getFrequency());
        values.put("notes", med.getNotes());
        values.put("status", med.getStatus());
        values.put("createdAt", med.getCreatedAt());
        values.put("alarmId", med.getAlarmId());
        values.put("quantity", med.getQuantity());
        values.put("lowStockLevel", med.getLowStockLevel());
        values.put("mealRelation", med.getMealRelation());
        values.put("startDate", med.getStartDate());
        values.put("endDate", med.getEndDate());
        values.put("todayOnly", med.isTodayOnly() ? 1 : 0);
        long id = db.insert("medicines", null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public List<Medicine> getAllMedicines() {
        List<Medicine> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM medicines ORDER BY reminderTime ASC", null);
        if (cursor.moveToFirst()) {
            do {
                Medicine med = new Medicine();
                med.setId(cursor.getInt(cursor.getColumnIndex("id")));
                med.setName(cursor.getString(cursor.getColumnIndex("name")));
                med.setDosage(cursor.getString(cursor.getColumnIndex("dosage")));
                med.setType(cursor.getString(cursor.getColumnIndex("type")));
                med.setReminderTime(cursor.getString(cursor.getColumnIndex("reminderTime")));
                med.setFrequency(cursor.getString(cursor.getColumnIndex("frequency")));
                med.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
                med.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                med.setCreatedAt(cursor.getString(cursor.getColumnIndex("createdAt")));
                med.setAlarmId(cursor.getInt(cursor.getColumnIndex("alarmId")));
                med.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));
                med.setLowStockLevel(cursor.getInt(cursor.getColumnIndex("lowStockLevel")));
                med.setMealRelation(cursor.getString(cursor.getColumnIndex("mealRelation")));
                med.setStartDate(cursor.getString(cursor.getColumnIndex("startDate")));
                med.setEndDate(cursor.getString(cursor.getColumnIndex("endDate")));
                med.setTodayOnly(cursor.getInt(cursor.getColumnIndex("todayOnly")) == 1);
                list.add(med);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    
    @SuppressLint("Range")
    public Medicine getMedicine(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM medicines WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            Medicine med = new Medicine();
            med.setId(cursor.getInt(cursor.getColumnIndex("id")));
            med.setName(cursor.getString(cursor.getColumnIndex("name")));
            med.setDosage(cursor.getString(cursor.getColumnIndex("dosage")));
            med.setType(cursor.getString(cursor.getColumnIndex("type")));
            med.setReminderTime(cursor.getString(cursor.getColumnIndex("reminderTime")));
            med.setFrequency(cursor.getString(cursor.getColumnIndex("frequency")));
            med.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
            med.setStatus(cursor.getString(cursor.getColumnIndex("status")));
            med.setCreatedAt(cursor.getString(cursor.getColumnIndex("createdAt")));
            med.setAlarmId(cursor.getInt(cursor.getColumnIndex("alarmId")));
            med.setQuantity(cursor.getInt(cursor.getColumnIndex("quantity")));
            med.setLowStockLevel(cursor.getInt(cursor.getColumnIndex("lowStockLevel")));
            med.setMealRelation(cursor.getString(cursor.getColumnIndex("mealRelation")));
            med.setStartDate(cursor.getString(cursor.getColumnIndex("startDate")));
            med.setEndDate(cursor.getString(cursor.getColumnIndex("endDate")));
            med.setTodayOnly(cursor.getInt(cursor.getColumnIndex("todayOnly")) == 1);
            cursor.close();
            db.close();
            return med;
        }
        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public int updateMedicine(Medicine med) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", med.getName());
        values.put("dosage", med.getDosage());
        values.put("type", med.getType());
        values.put("reminderTime", med.getReminderTime());
        values.put("frequency", med.getFrequency());
        values.put("notes", med.getNotes());
        values.put("status", med.getStatus());
        values.put("alarmId", med.getAlarmId());
        values.put("quantity", med.getQuantity());
        values.put("lowStockLevel", med.getLowStockLevel());
        values.put("mealRelation", med.getMealRelation());
        values.put("startDate", med.getStartDate());
        values.put("endDate", med.getEndDate());
        values.put("todayOnly", med.isTodayOnly() ? 1 : 0);
        int rows = db.update("medicines", values, "id=?", new String[]{String.valueOf(med.getId())});
        db.close();
        return rows;
    }

    public void decreaseStock(int medicineId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE medicines SET quantity = quantity - 1 WHERE id = ? AND quantity > 0", new Object[]{medicineId});
        db.close();
    }

    public void deleteMedicine(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("medicines", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
    
    public void addHistory(History history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("medicineId", history.getMedicineId());
        values.put("medicineName", history.getMedicineName());
        values.put("dosage", history.getDosage());
        values.put("scheduledTime", history.getScheduledTime());
        values.put("takenDate", history.getTakenDate());
        values.put("takenTime", history.getTakenTime());
        values.put("status", history.getStatus());
        db.insert("history", null, values);
        db.close();
    }
    
    @SuppressLint("Range")
    public List<History> getAllHistory() {
        List<History> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM history ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                History h = new History();
                h.setId(cursor.getInt(cursor.getColumnIndex("id")));
                h.setMedicineId(cursor.getInt(cursor.getColumnIndex("medicineId")));
                h.setMedicineName(cursor.getString(cursor.getColumnIndex("medicineName")));
                h.setDosage(cursor.getString(cursor.getColumnIndex("dosage")));
                h.setScheduledTime(cursor.getString(cursor.getColumnIndex("scheduledTime")));
                h.setTakenDate(cursor.getString(cursor.getColumnIndex("takenDate")));
                h.setTakenTime(cursor.getString(cursor.getColumnIndex("takenTime")));
                h.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                list.add(h);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    
    @SuppressLint("Range")
    public boolean isDoseTakenToday(int medicineId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM history WHERE medicineId=? AND takenDate=? AND status='TAKEN'", 
            new String[]{String.valueOf(medicineId), date});
        boolean taken = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return taken;
    }
}
