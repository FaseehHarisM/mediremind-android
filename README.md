# 💊 MediRemind

**Smart Offline Medication Reminder, Tracking & Care Support System**  
*Remember. Take. Track. Care.*

---

## 📖 Overview

**MediRemind** is a modern, professional, and 100% offline Android application designed to help users manage their medication schedules, track adherence, and monitor medicine stock. 

Built as an **MCA Mobile Application Development Lab project**, it avoids complex cloud infrastructures in favor of robust, local Android features like `AlarmManager`, `SQLite`, and native `NotificationManager`. It is designed with a premium "Medical Blue" UI and features specialized application modes tailored for standard users, the elderly, caregivers, and doctors.

---

## ✨ Key Features

### 📅 Advanced Scheduling
* **Multiple Schedules:** Add the same medicine multiple times a day.
* **Date Ranges:** Set specific start and end dates or choose "Today Only".
* **Meal Instructions:** Tag medicines with meal requirements (e.g., *Before Breakfast*, *After Dinner*).

### 🔔 Reliable Offline Reminders
* **Local Notifications:** Triggered by Android `AlarmManager`—works even in Airplane mode.
* **Escalating Alerts:** Snooze reminders or receive escalating warnings for un-taken medication.
* **Actionable Notifications:** Mark medicines as "Taken" or "Snoozed" directly from the lock screen.

### 👥 4 Distinct Application Modes
1. **Standard Mode:** Modern dashboard with daily progress rings and upcoming schedules.
2. **Elderly Mode:** High-contrast, giant buttons, and simplified UI for accessibility.
3. **Caregiver Mode:** Special dashboard focusing on a patient's missed doses and low-stock warnings.
4. **Doctor Mode:** Clinical view focusing on medication adherence reports and active prescriptions.

### 📊 Tracking & Reporting
* **Stock Management:** Automatically deducts pill quantity when taken. Alerts you when stock falls below your custom threshold.
* **History Filters:** View medication history filtered by Today, Yesterday, Last 7 Days, or specific statuses (Taken/Missed).
* **Native PDF Export:** Generates an A4 medication adherence report directly to your phone's local storage using native Android APIs.
* **Doctor Calling:** Save doctor/emergency contacts and dial them directly using Android Implicit Intents.

---

## 🛠️ Technology Stack

* **Environment:** Android Studio
* **Language:** Java
* **UI/UX:** XML, Material Components, Custom Vector Drawables
* **Database:** SQLite (`SQLiteOpenHelper`)
* **Local Storage:** `SharedPreferences`
* **Background Tasks:** `AlarmManager`, `BroadcastReceiver`, `PendingIntent`
* **Reporting:** `android.graphics.pdf.PdfDocument`
* **Architecture:** Offline-first Native Android

---

## 🚀 Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/MediRemind.git
   ```
2. **Open the project:**
   * Launch Android Studio.
   * Select **File > Open** and choose the `MediRemind` directory.
3. **Build the project:**
   * Let Gradle sync dependencies.
   * If you are on a strict corporate network, ensure your SSL/TLS certificates are correctly configured in `gradle.properties`.
4. **Run the App:**
   * Connect an Android device or start an Emulator.
   * Click the **Run** button (`Shift + F10`).

---

## 🎨 UI & Design Philosophy

The application strictly adheres to a **Modern Medical** aesthetic:
* **Primary Color:** Deep Medical Blue (`#1565C0`)
* **Background:** Soft Blue (`#F5F9FF`) to reduce eye strain.
* **Cards:** Elevated white surfaces with smooth rounded corners (`16dp` - `20dp`).
* **Icons:** High-quality Material Vector drawables (No pixelated bitmaps!).

---

## 🎓 Academic Requirements Fulfilled

This project demonstrates proficiency in fundamental Android components:
* **Activity Lifecycle:** Handled across 8+ unique screens.
* **Intents:** Explicit intents for navigation; Implicit intents for the phone dialer.
* **Storage:** Full CRUD operations via SQLite and simple key-value via SharedPreferences.
* **Services/Receivers:** Proper implementation of `BroadcastReceiver` for handling background alarms while the app is closed.
* **UI Components:** `RecyclerView` (with custom adapters), `TimePicker`, `DatePicker`, `CardView`, and Material form validation.

---

## 📄 License

This project is open-source and created for educational purposes. Feel free to fork and modify!
