package com.example.daniel.database.dataoldtrainings;

import android.provider.BaseColumns;

/**
 * Created by Daniel on 2017-05-19.
 */

public interface OldTrainingsColumnNames extends BaseColumns {
    public static String TABLE_NAME=null;
    public static final String TRAINING_ID = "training_id";
    public static final String EXERCISE_ID = "exercise_id";
    public static final String ROUND_NUMBER = "round_number";
    public static final String REPS = "reps";
    public static final String WEIGHT = "weight";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String DURATION ="duration";
}
