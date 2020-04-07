package com.example.daniel.gymassistant.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_name")
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public int _ID;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "language")
    public String language;
}
