package com.example.mediremind.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mediremind.R;
import com.example.mediremind.activities.MedicineDetailsActivity;
import com.example.mediremind.models.Medicine;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private List<Medicine> medicineList;
    private Context context;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine med = medicineList.get(position);
        holder.tvMedicineName.setText(med.getName());
        
        String meal = med.getMealRelation() != null && !med.getMealRelation().isEmpty() ? " • " + med.getMealRelation() : "";
        holder.tvDosageType.setText(med.getDosage() + " • " + med.getType() + meal);
        
        holder.tvReminderTime.setText(med.getReminderTime());

        android.graphics.drawable.GradientDrawable bgShape = (android.graphics.drawable.GradientDrawable) holder.tvStatus.getBackground();

        if ("TAKEN".equals(med.getStatus())) {
            holder.tvStatus.setText("TAKEN");
            bgShape.setColor(Color.parseColor("#E8F5E9"));
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else if ("MISSED".equals(med.getStatus())) {
            holder.tvStatus.setText("MISSED");
            bgShape.setColor(Color.parseColor("#FFEBEE"));
            holder.tvStatus.setTextColor(Color.parseColor("#D32F2F"));
        } else {
            holder.tvStatus.setText("UPCOMING");
            bgShape.setColor(Color.parseColor("#FFF3E0"));
            holder.tvStatus.setTextColor(Color.parseColor("#EF6C00"));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicineDetailsActivity.class);
            intent.putExtra("MEDICINE_ID", med.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }
    
    public void updateList(List<Medicine> newList) {
        medicineList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicineName, tvDosageType, tvReminderTime, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedicineName = itemView.findViewById(R.id.tvMedicineName);
            tvDosageType = itemView.findViewById(R.id.tvDosageType);
            tvReminderTime = itemView.findViewById(R.id.tvReminderTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
