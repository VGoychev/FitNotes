package com.example.fitnotes.exercise;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnotes.exercise.ExerciseItem;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exerciseitem WHERE workout_id = :workoutId") // Modify this query as per your database structure
    List<ExerciseItem> getExercisesForWorkout(int workoutId);
    @Insert
    void insertExerciseItem(ExerciseItem... exerciseItems);
    @Delete
    void delete(ExerciseItem exerciseItem);

}
