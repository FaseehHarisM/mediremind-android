package com.example.mediremind.models;

public class History {
    private int id;
    private int medicineId;
    private String medicineName;
    private String dosage;
    private String scheduledTime;
    private String takenDate;
    private String takenTime;
    private String status;

    public History() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }
    public String getTakenDate() { return takenDate; }
    public void setTakenDate(String takenDate) { this.takenDate = takenDate; }
    public String getTakenTime() { return takenTime; }
    public void setTakenTime(String takenTime) { this.takenTime = takenTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
