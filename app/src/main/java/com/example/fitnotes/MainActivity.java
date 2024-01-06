package com.example.fitnotes;
import static android.media.CamcorderProfile.get;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;


import com.example.fitnotes.exercise.ExerciseItem;
import com.example.fitnotes.workout.AppDatabase;
import com.example.fitnotes.workout.WorkoutAdapter;
import com.example.fitnotes.workout.WorkoutInterface;
import com.example.fitnotes.workout.WorkoutItem;

import java.util.ArrayList;

import java.util.List;


public class MainActivity extends AppCompatActivity implements WorkoutInterface{
    RecyclerView recyclerView;
    Button btnAddWorkout;
    TextView txtViewInstructions;
    private WorkoutAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnAddWorkout = findViewById(R.id.btnAddWorkout);
        txtViewInstructions = findViewById(R.id.txtViewInstructions);
        btnAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddWorkoutDialog();
            }
        });
        initRecyclerView();

        loadWorkoutList();
        updateInstructionsVisibility();
        recyclerViewAdapter.setWorkoutInterface(this);
    }
        @Override
    public void onItemClick(WorkoutItem workoutItem) {
            Intent intent = new Intent(MainActivity.this, Workout.class);
            intent.putExtra("WORKOUT_NAME", workoutItem.getWorkoutName()); // Pass workout details
            intent.putExtra("WORKOUT_ID", workoutItem.getId());
            startActivity(intent);
    }
    private void showAddWorkoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogBackground);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_workout, null);
        builder.setView(dialogView);

        final EditText workoutNameEditText = dialogView.findViewById(R.id.editText_workout_name);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String workoutName = workoutNameEditText.getText().toString().trim();

                if (!workoutName.isEmpty()) {
                    saveNewWorkout(workoutName);
                    loadWorkoutList();
                    recyclerViewAdapter.notifyDataSetChanged();
                    updateInstructionsVisibility();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveNewWorkout(String workoutName) {
        AppDatabase database = AppDatabase.getInstance(this.getApplicationContext());
        WorkoutItem workoutItem = new WorkoutItem();
        workoutItem.workoutName = workoutName;
        database.workoutDao().insertWorkoutItem(workoutItem);
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_workout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerViewAdapter = new WorkoutAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback());
        ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(new DragAndDropCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
        itemTouchHelper1.attachToRecyclerView(recyclerView);
    }

    private void loadWorkoutList() {
        AppDatabase database = AppDatabase.getInstance(this.getApplicationContext());
        List<WorkoutItem> workoutList = database.workoutDao().getAllWorkoutItems();
        recyclerViewAdapter.setWorkoutList(workoutList);
    }

    private class DragAndDropCallback extends ItemTouchHelper.SimpleCallback{
        DragAndDropCallback(){
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN,0);
        }
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            // Notify the adapter of the move
            recyclerViewAdapter.moveWorkoutItem(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

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
            WorkoutItem deletedItem = recyclerViewAdapter.getWorkoutList().get(position);
            deleteItem(deletedItem);
            updateInstructionsVisibility();
        }
    }
        private void deleteItem(WorkoutItem workoutItem) {
            // Remove item from RecyclerView
            AppDatabase database = AppDatabase.getInstance(this.getApplicationContext());
            database.workoutDao().delete(workoutItem);
            recyclerViewAdapter.removeItem(workoutItem);

        }

    private void updateInstructionsVisibility() {
    if (recyclerViewAdapter.getItemCount() == 0) {
        txtViewInstructions.setVisibility(View.VISIBLE);
    } else {
        txtViewInstructions.setVisibility(View.GONE);
    }
}

}