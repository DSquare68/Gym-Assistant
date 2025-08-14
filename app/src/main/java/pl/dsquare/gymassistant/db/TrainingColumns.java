package pl.dsquare.gymassistant.db;

import androidx.room.ColumnInfo;

public interface TrainingColumns {
    String TABLE_NAME = "TRAININGS";
    String ID = "ID";
    String TRAINING_INFO_ID = "ID_TRAINING";
    String EXERCISE_NAME_ID = "ID_EXERCISE_NAME";
    String DATE = "DATE_TRAINING";
    String WEIGHT = "WEIGHT";
    String REPS = "REPEAT";
    String SERIE = "SERIE";
    String SCHEMA ="NAME_SCHEMA";
    String IS_SCHEMA = "IS_SCHEMA";
}
