package com.example.mediremind.activities;

import android.app.DatePickerDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mediremind.R;
import com.example.mediremind.database.DatabaseHelper;
import com.example.mediremind.models.History;
import com.example.mediremind.utils.DateTimeHelper;
import com.google.android.material.button.MaterialButton;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private String startDate, endDate;
    private MaterialButton btnStart, btnEnd;
    private TextView tvStatus;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        
        db = new DatabaseHelper(this);
        startDate = DateTimeHelper.getCurrentDate();
        endDate = DateTimeHelper.getCurrentDate();
        
        btnStart = findViewById(R.id.btnReportStart);
        btnEnd = findViewById(R.id.btnReportEnd);
        tvStatus = findViewById(R.id.tvReportStatus);
        MaterialButton btnGenerate = findViewById(R.id.btnGeneratePDF);
        
        btnStart.setText(startDate);
        btnEnd.setText(endDate);
        
        btnStart.setOnClickListener(v -> showDatePicker(true));
        btnEnd.setOnClickListener(v -> showDatePicker(false));
        
        btnGenerate.setOnClickListener(v -> generatePDF());
    }
    
    private void showDatePicker(boolean isStart) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            if (isStart) {
                startDate = formattedDate;
                btnStart.setText(startDate);
            } else {
                endDate = formattedDate;
                btnEnd.setText(endDate);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
    
    private void generatePDF() {
        List<History> allHistory = db.getAllHistory();
        
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size
        PdfDocument.Page page = document.startPage(pageInfo);
        
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        
        // Title
        paint.setColor(Color.parseColor("#1565C0"));
        paint.setTextSize(24f);
        paint.setFakeBoldText(true);
        canvas.drawText("MediRemind Medication Report", 40, 50, paint);
        
        paint.setColor(Color.BLACK);
        paint.setTextSize(14f);
        paint.setFakeBoldText(false);
        canvas.drawText("Date Range: " + startDate + " to " + endDate, 40, 80, paint);
        
        // Table Header
        int y = 120;
        paint.setFakeBoldText(true);
        canvas.drawText("Date", 40, y, paint);
        canvas.drawText("Time", 120, y, paint);
        canvas.drawText("Medicine", 200, y, paint);
        canvas.drawText("Status", 400, y, paint);
        
        paint.setFakeBoldText(false);
        y += 20;
        canvas.drawLine(40, y, 550, y, paint);
        y += 20;
        
        int taken = 0, missed = 0, skipped = 0;
        
        // Draw rows
        for (History h : allHistory) {
            // Filter by date range
            if (h.getTakenDate().compareTo(startDate) >= 0 && h.getTakenDate().compareTo(endDate) <= 0) {
                canvas.drawText(h.getTakenDate(), 40, y, paint);
                canvas.drawText(h.getTakenTime() != null ? h.getTakenTime() : h.getScheduledTime(), 120, y, paint);
                canvas.drawText(h.getMedicineName(), 200, y, paint);
                canvas.drawText(h.getStatus(), 400, y, paint);
                
                if ("TAKEN".equals(h.getStatus())) taken++;
                else if ("MISSED".equals(h.getStatus())) missed++;
                else if ("SKIPPED".equals(h.getStatus())) skipped++;
                
                y += 25;
                if (y > 800) { // Simple pagination break logic
                    document.finishPage(page);
                    pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 2).create();
                    page = document.startPage(pageInfo);
                    canvas = page.getCanvas();
                    y = 50;
                }
            }
        }
        
        y += 20;
        canvas.drawLine(40, y, 550, y, paint);
        y += 30;
        
        // Summary
        paint.setFakeBoldText(true);
        canvas.drawText("Summary:", 40, y, paint);
        paint.setFakeBoldText(false);
        canvas.drawText("Total Taken: " + taken, 40, y + 25, paint);
        canvas.drawText("Total Missed: " + missed, 40, y + 50, paint);
        canvas.drawText("Total Skipped: " + skipped, 40, y + 75, paint);
        
        document.finishPage(page);
        
        try {
            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, "MediRemind_Report_" + System.currentTimeMillis() + ".pdf");
            document.writeTo(new FileOutputStream(file));
            tvStatus.setText("PDF Saved Successfully to Documents Folder!");
            Toast.makeText(this, "Report Generated!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            tvStatus.setText("Failed to generate PDF.");
        }
        
        document.close();
    }
}
