package com.example.fitnotes.workout;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitnotes.exercise.ExerciseDao;
import com.example.fitnotes.exercise.ExerciseItem;
import com.example.fitnotes.set.SetDao;
import com.example.fitnotes.set.SetItem;

@Database(entities = {WorkoutItem.class, ExerciseItem.class, SetItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WorkoutDao workoutDao();
    public abstract ExerciseDao exerciseDao();
    public abstract SetDao setDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }
}
