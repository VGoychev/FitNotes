package com.example.fitnotes.exercise;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.fitnotes.workout.WorkoutItem;

@Entity(foreignKeys = @ForeignKey(entity = WorkoutItem.class,
                parentColumns = "id",
                childColumns = "workout_id",
                onDelete = ForeignKey.CASCADE))
public class ExerciseItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "exercise_name")
    public String exerciseName;
    @ColumnInfo(name = "workout_id")
    public int workoutId;
    @ColumnInfo(name = "position")
    public int position;
    public void setPosition(int newPosition) {
        this.position = newPosition;
    }

    public int getPosition() {
        return position;
    }
    public int getId() {
        return id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public int getWorkoutId() {
        return workoutId;
    }
}
