package com.example.daniel.gymassistant.db;

import androidx.room.ColumnInfo;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

public interface TrainingDao {

    @Query("SELECT * FROM training")
    List<Exercise> getAll();

    @Query("SELECT * FROM training WHERE _ID IN (:exerciseIds)")
    List<Exercise> loadAllByIds(int[] exerciseIds);

    @Query("SELECT * FROM training WHERE  exercises = :first")
    Exercise findByExercise(int first);

    @Insert
    void insertAll(Exercise... exercises);

    @Delete
    void delete(Exercise user);

}
