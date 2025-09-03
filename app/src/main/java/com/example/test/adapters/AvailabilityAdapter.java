package com.example.test.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.models.Availability;

import java.util.List;

public class AvailabilityAdapter extends RecyclerView.Adapter<AvailabilityAdapter.ViewHolder> {

    private List<Availability> availabilities;

    public AvailabilityAdapter(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_availability, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Availability a = availabilities.get(position);
        holder.dateText.setText(a.getDate() + " (" + a.getDayOfWeek() + ")");
        holder.timeText.setText(a.getTimeSlot());
        holder.doctorText.setText("Doctor ID: " + a.getDoctorId());
    }

    @Override
    public int getItemCount() {
        return availabilities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, timeText, doctorText;
        ViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            doctorText = itemView.findViewById(R.id.doctorText);
        }
    }
}
