package com.example.fitnotes.workout;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnotes.R;

import java.util.Collections;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private List<WorkoutItem> workoutList;
    private Context context;
    private WorkoutInterface workoutInterface;

    public void setWorkoutInterface(WorkoutInterface workoutInterface) {
        this.workoutInterface = workoutInterface;
    }
    public void setWorkoutList(List<WorkoutItem> workoutList){
        this.workoutList = workoutList;
        notifyDataSetChanged();
    }
    public List<WorkoutItem> getWorkoutList() {
        return workoutList;
    }
    public void removeItem(WorkoutItem workoutItem) {
        int position = workoutList.indexOf(workoutItem);
        if (position != -1) {
            workoutList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public WorkoutAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView_workout_name.setText(this.workoutList.get(position).workoutName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workoutInterface != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        workoutInterface.onItemClick(workoutList.get(position));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.workoutList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_workout_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_workout_name = itemView.findViewById(R.id.textView_workout);

        }

    }
}