package com.example.fitnotes.workout;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Query("SELECT * FROM workoutitem")
    List<WorkoutItem> getAllWorkoutItems();
    @Query("SELECT * FROM workoutitem WHERE id = :workoutId")
    WorkoutItem getWorkoutById(int workoutId);
    @Query("UPDATE workoutitem SET position = :newPosition WHERE id = :id")
    void updateWorkoutItemPosition(int id, int newPosition);
    @Update
    void updateWorkoutItem(WorkoutItem workoutItem);

    @Insert
    void insertWorkoutItem(WorkoutItem... workoutItems);
    @Delete
    void delete(WorkoutItem workoutItem);
}
