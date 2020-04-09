package com.example.daniel.gymassistant.db;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract interface TrainingDao {

    @Query("SELECT * FROM training")
    List<Training> getAll();

    @Query("SELECT * FROM training WHERE _ID IN (:exerciseIds)")
    List<Training> loadAllByIds(int[] exerciseIds);

    @Query("SELECT * FROM training WHERE  exercises = :first")
    Training findByExercise(int first);

    @Insert
    void insertAll(Training... exercises);

    @Delete
    void delete(Training user);

}
