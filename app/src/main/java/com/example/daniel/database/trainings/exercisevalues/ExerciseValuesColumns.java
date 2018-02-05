package com.example.daniel.database.trainings.exercisevalues;

import android.provider.BaseColumns;

/**
 * Created by Daniel on 2017-04-22.
 */

public interface ExerciseValuesColumns extends BaseColumns {
    public static final String TABLE_NAME ="exercise_values";
    public static final String EXERCISE_ID = "nexercise_id";
    public static final String TRANING_ID = "training_id";
    public static final String ROUND_NUMBER ="round_number";
    public static final String WEIGHT = "weight";
    public static final String REPS ="reps";
    public static final String EXERCISE_NUMBER ="Eexercise_number";

}
