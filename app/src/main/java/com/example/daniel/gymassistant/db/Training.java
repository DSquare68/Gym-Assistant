package com.example.daniel.gymassistant.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "training")
public class Training {

    @PrimaryKey(autoGenerate = true)
    public int _ID;

    @ColumnInfo(name = "training_info_ID")
    public int trainingInfoID;

    @ColumnInfo(name = "exercises")
    public int exercises;

    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "weight")
    public double weight;

    @ColumnInfo(name = "reps")
    public double reps;

    @ColumnInfo(name = "series")
    public double series;

}

