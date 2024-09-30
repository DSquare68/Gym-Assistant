package pl.dsquare.gymassistant.db;

import androidx.room.ColumnInfo;

public interface TrainingColumns {
    String TABLE_NAME = "Training";
    String ID = "_ID";
    String TRAINING_INFO_ID = "training_info_id";
    String EXERCISE = "exercise";
    String DATE = "date";
    String WEIGHT = "weight";
    String REPS = "rep";
    String SERIE = "serie";
    String TEMPLATE_NUMBER = "template_family";
    String SCHEMA ="schema";
}
