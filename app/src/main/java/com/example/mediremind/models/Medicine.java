package com.example.mediremind.models;

import java.io.Serializable;

public class Medicine implements Serializable {
    private int id;
    private String name;
    private String dosage;
    private String type;
    private String reminderTime;
    private String frequency;
    private String notes;
    private String status;
    private String createdAt;
    private int alarmId;
    
    // Stock Tracking fields
    private int quantity;
    private int lowStockLevel;
    
    // Core PRD fields missing previously
    private String mealRelation;
    private String startDate;
    private String endDate;
    private boolean todayOnly;

    public Medicine() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getReminderTime() { return reminderTime; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public int getAlarmId() { return alarmId; }
    public void setAlarmId(int alarmId) { this.alarmId = alarmId; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getLowStockLevel() { return lowStockLevel; }
    public void setLowStockLevel(int lowStockLevel) { this.lowStockLevel = lowStockLevel; }
    
    public String getMealRelation() { return mealRelation; }
    public void setMealRelation(String mealRelation) { this.mealRelation = mealRelation; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public boolean isTodayOnly() { return todayOnly; }
    public void setTodayOnly(boolean todayOnly) { this.todayOnly = todayOnly; }
}
