package com.example.daniel.database.trainings.trainingvalues;

import android.provider.BaseColumns;
import android.widget.Spinner;

/**
 * Created by Daniel on 2017-04-25.
 */

public interface TrainingValuesColumns extends BaseColumns {
    public static final String TABLE_NAME ="training_values";
    public static final String TRAINING_ID = "training_id";
    public static final String EXERCISE_NUMBER = "exercise_number";
    public static final String ROUNDS_NUMBER = "rounds_number";
    public static final String TRAINING_MODE = "training_mode";
    public static final String SCHEDULE = "schedule";
    public static final String WEEK_DAYS ="week_days";
    public static final String ADD_DATE = "add_date";
    public static final String FIRST_DAY_DATE = "first_day_training";
    public static final String LAST_TRAINING_DAY_DATE = "last_training_day_date";
    public static final String REPETITION ="repetition";
    public static final String AVERAGE_TIME="average_time";

}
