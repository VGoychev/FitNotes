package com.example.fitnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fitnotes.exercise.ExerciseAdapter;
import com.example.fitnotes.exercise.ExerciseItem;
import com.example.fitnotes.workout.AppDatabase;
import com.example.fitnotes.workout.WorkoutItem;

import java.util.List;

public class Workout extends AppCompatActivity {
    Button btnAddExercise;
    RecyclerView recyclerView;
    TextView textViewWorkoutName, textViewInstructions;
    ExerciseAdapter recyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        btnAddExercise = findViewById(R.id.btnAddExercise);
        recyclerView = findViewById(R.id.recycler_exercise);
        textViewWorkoutName = findViewById(R.id.textView_workoutName);
        textViewInstructions = findViewById(R.id.textViewInstructions);

        String selectedWorkoutName = getIntent().getStringExtra("WORKOUT_NAME");

        textViewWorkoutName.setText(selectedWorkoutName);

        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddExerciseDialog();
            }
        });
        initRecyclerView();
        loadExerciseList();
        updateInstructionsVisibility();
    }
    private void showAddExerciseDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogBackground);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_exercise, null);
        builder.setView(dialogView);

        final EditText exerciseNameEditText = dialogView.findViewById(R.id.editText_exercise_name);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String exerciseName = exerciseNameEditText.getText().toString().trim();
                if (!exerciseName.isEmpty()){
                    int workoutId = getIntent().getIntExtra("WORKOUT_ID",-1);
                    if (workoutId != -1) {
                    saveNewExercise(exerciseName,workoutId);
                    loadExerciseList();
                    recyclerViewAdapter.notifyDataSetChanged();
                    updateInstructionsVisibility();
                    } else {

                    }
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);

                positiveButton.setTextColor(getResources().getColor(R.color.white));
                negativeButton.setTextColor(getResources().getColor(R.color.white));


            }
        });
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();
    }
    private void updateInstructionsVisibility() {
        if (recyclerViewAdapter.getItemCount() == 0) {
            textViewInstructions.setVisibility(View.VISIBLE);
        } else {
            textViewInstructions.setVisibility(View.GONE);
        }
    }
    private void deleteItem(ExerciseItem exerciseItem) {

        AppDatabase database = AppDatabase.getInstance(this.getApplicationContext());
        database.exerciseDao().delete(exerciseItem);
        recyclerViewAdapter.removeExerciseItem(exerciseItem);

    }
    private void saveNewExercise(String exerciseName, int workoutId) {
        AppDatabase database = AppDatabase.getInstance(this.getApplicationContext());
        workoutId = getIntent().getIntExtra("WORKOUT_ID", -1);
        try {
        WorkoutItem workoutItem = database.workoutDao().getWorkoutById(workoutId);
            if (workoutItem != null) {
            // The workoutId exists, proceed with saving ExerciseItem
            ExerciseItem exerciseItem = new ExerciseItem();
            exerciseItem.exerciseName = exerciseName;
            exerciseItem.workoutId = workoutId;
            database.exerciseDao().insertExerciseItem(exerciseItem);
        } else {

        }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_exercise);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerViewAdapter = new ExerciseAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new Workout.SwipeToDeleteCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void loadExerciseList() {
        AppDatabase database = AppDatabase.getInstance(this.getApplicationContext());
        int selectedWorkoutId = getIntent().getIntExtra("WORKOUT_ID", -1);
        if (selectedWorkoutId != -1) {
            // Fetch exercises associated with the selected workout ID
            List<ExerciseItem> exercisesForWorkout = database.exerciseDao().getExercisesForWorkout(selectedWorkoutId);
            recyclerViewAdapter.setExerciseList(exercisesForWorkout);
            recyclerViewAdapter.notifyDataSetChanged();
        } else {
            Log.e("WorkoutActivity", "No workout ID found in the intent");
        }
    }
    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        SwipeToDeleteCallback() {
            super(0, ItemTouchHelper.LEFT);
        }
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            ExerciseItem deletedItem = recyclerViewAdapter.getExerciseList().get(position);
            deleteItem(deletedItem);
            updateInstructionsVisibility();
        }
    }


}