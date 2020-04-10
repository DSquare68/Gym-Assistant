package com.example.daniel.gymassistant.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "training_info")
public class TrainingInfo {

    @PrimaryKey(autoGenerate = true)
    public int _ID;

    @ColumnInfo(name = "training_name")
    public String trainingName;

    @ColumnInfo(name = "training_date")
    public int trainingDate;

    //TODO duration

}
