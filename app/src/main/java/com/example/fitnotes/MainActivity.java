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
import android.widget.CalendarView;
import android.widget.EditText;

import android.widget.TextView;


import com.example.fitnotes.exercise.ExerciseItem;
import com.example.fitnotes.workout.AppDatabase;
import com.example.fitnotes.workout.WorkoutAdapter;
import com.example.fitnotes.workout.WorkoutInterface;
import com.example.fitnotes.workout.WorkoutItem;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements WorkoutInterface{
    RecyclerView recyclerView;
    Button btnAddWorkout;
    TextView txtViewInstructions;
    private WorkoutAdapter recyclerViewAdapter;
    CalendarView calendarView;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnAddWorkout = findViewById(R.id.btnAddWorkout);
        txtViewInstructions = findViewById(R.id.txtViewInstructions);
        calendarView = findViewById(R.id.calendarWorkouts);
        calendar = Calendar.getInstance();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });
        btnAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddWorkoutDialog();
            }
        });
        initRecyclerView();
        setDate();
        loadWorkoutList();
        updateInstructionsVisibility();
        recyclerViewAdapter.setWorkoutInterface(this);
    }
    public void setDate(){
        long milli = System.currentTimeMillis();
        calendar.setTimeInMillis(milli);
        calendarView.setDate(milli);
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
        updateWorkoutItemPositions(workoutList);
        workoutList = database.workoutDao().getAllWorkoutItems();

        recyclerViewAdapter.setWorkoutList(workoutList);
        recyclerViewAdapter.notifyDataSetChanged();
    }
    private void updateWorkoutItemPositions(List<WorkoutItem> workoutList) {
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        database.runInTransaction(() -> {
        for (int i = 0; i < workoutList.size(); i++) {
            WorkoutItem workoutItem = workoutList.get(i);
            workoutItem.setPosition(i); // Update the position in the WorkoutItem

            // Update the WorkoutItem in the database
            database.workoutDao().updateWorkoutItemPosition(workoutItem.getId(), workoutItem.getPosition());
        }
        });
    }

    private class DragAndDropCallback extends ItemTouchHelper.SimpleCallback{
        DragAndDropCallback(){
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN,0);
        }
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            List<WorkoutItem> workoutList = recyclerViewAdapter.getWorkoutList();
            if (workoutList != null && fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION) {

                WorkoutItem movedItem = workoutList.get(fromPosition);

                workoutList.remove(fromPosition);

                workoutList.add(toPosition, movedItem);

                // Notify adapter of the item movement
                recyclerViewAdapter.notifyItemMoved(fromPosition, toPosition);

                // Update positions in the database
                updateWorkoutItemPositions(workoutList);
            }
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