package com.example.daniel.database.exercise.values;

import android.provider.BaseColumns;

/**
 * Created by Daniel on 2017-04-22.
 */

public interface ExerciseValuesColumns extends BaseColumns {
    String TABLE_NAME ="exercise_values";
    String EXERCISE_ID = "exercise_id";
    String TRAINING_ID = "training_id";
    String ROUND_NUMBER ="round_number";
    String WEIGHT = "weight";
    String REPS ="reps";
    String EXERCISE_NUMBER ="exercise_number";

}
