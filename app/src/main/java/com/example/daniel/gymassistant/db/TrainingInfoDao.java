package com.example.daniel.gymassistant.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TrainingInfoDao {

    @Query("SELECT * FROM training_info")
    List<TrainingInfo> getAll();

    @Query("SELECT * FROM training_info WHERE _ID IN (:exerciseIds)")
    List<TrainingInfo> loadAllByIds(int[] exerciseIds);

    @Query("SELECT * FROM training_info WHERE  training_name = :first")
    TrainingInfo findByExerciseName(String first);

    @Insert
    void insertAll(TrainingInfo... exercises);

    @Delete
    void delete(TrainingInfo user);
}
