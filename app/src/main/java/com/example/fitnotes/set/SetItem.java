package com.example.fitnotes.set;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.fitnotes.exercise.ExerciseItem;
import com.example.fitnotes.workout.WorkoutItem;

@Entity(foreignKeys = @ForeignKey(entity = ExerciseItem.class,
        parentColumns = "id",
        childColumns = "exercise_id",
        onDelete = ForeignKey.CASCADE))
public class SetItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "exercise_rep")
    public int rep;
    @ColumnInfo(name = "exercise_weight")
    public double weight;
    @ColumnInfo(name = "exercise_id")
    public int exercise_id;
    @ColumnInfo(name = "stored_rep")
    public int storedRep;
    @ColumnInfo(name = "stored_weight")
    public double storedWeight;

    public int getStoredRep() {
        return storedRep;
    }

    public double getStoredWeight() {
        return storedWeight;
    }

    public int getId() {
        return id;
    }

    public int getRep() {
        return rep;
    }
    public double getWeight() {
        return weight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setExercise_id(int exercise_id) {
        this.exercise_id = exercise_id;
    }

    public void setStoredRep(int storedRep) {
        this.storedRep = storedRep;
    }

    public void setStoredWeight(double storedWeight) {
        this.storedWeight = storedWeight;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public int getExercise_id(){
        return exercise_id;
    }
}
