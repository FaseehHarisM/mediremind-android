package com.example.mediremind.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mediremind.R;
import com.example.mediremind.models.History;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<History> historyList;
    private Context context;

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History h = historyList.get(position);
        holder.tvHistoryMedName.setText(h.getMedicineName());
        holder.tvHistoryDosage.setText(h.getDosage());
        
        if ("TAKEN".equals(h.getStatus())) {
            holder.ivStatus.setImageResource(R.drawable.ic_check_circle);
            holder.tvHistoryTime.setText("Taken at " + h.getTakenTime());
        } else {
            holder.ivStatus.setImageResource(R.drawable.ic_error); 
            holder.tvHistoryTime.setText("Missed at " + h.getScheduledTime());
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void updateList(List<History> newList) {
        historyList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistoryMedName, tvHistoryDosage, tvHistoryTime;
        ImageView ivStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistoryMedName = itemView.findViewById(R.id.tvHistoryMedName);
            tvHistoryDosage = itemView.findViewById(R.id.tvHistoryDosage);
            tvHistoryTime = itemView.findViewById(R.id.tvHistoryTime);
            ivStatus = itemView.findViewById(R.id.ivStatus);
        }
    }
}
