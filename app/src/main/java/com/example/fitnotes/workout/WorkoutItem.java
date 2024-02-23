package com.example.fitnotes.workout;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;
@Entity
public class WorkoutItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "workout_name")
    public String workoutName;
    @ColumnInfo(name = "position")
    public int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getWorkoutName() {
        return workoutName;
    }
    public int getId(){
        return id;
    }
}
