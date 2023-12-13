package com.example.fitnotes.exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnotes.R;
import com.example.fitnotes.workout.WorkoutAdapter;
import com.example.fitnotes.workout.WorkoutItem;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private List<ExerciseItem> exerciseList;
    private Context context;

    public void setExerciseList(List<ExerciseItem> exerciseList){
        this.exerciseList = exerciseList;
        notifyDataSetChanged();
    }
    public List<ExerciseItem> getExerciseList() {
        return exerciseList;
    }
    public void removeExerciseItem(ExerciseItem exerciseItem) {
        int position = exerciseList.indexOf(exerciseItem);
        if (position != -1) {
            exerciseList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public ExerciseAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_workout, parent, false);
        return new ExerciseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ViewHolder holder, int position) {
        holder.textView_exercise_name.setText(this.exerciseList.get(position).exerciseName);
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_exercise_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_exercise_name = itemView.findViewById(R.id.textView_workout);

        }

    }
}
