package pl.dsquare.home.android.db;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class OpenHelper extends SupportSQLiteOpenHelper.Callback {

    public OpenHelper(int version) {
        super(version);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ExerciseColumns.TABLE_NAME+" ( "+ExerciseColumns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ExerciseColumns.EXERCISE_NAME+" TEXT NOT NULL )");
        db.execSQL("CREATE TABLE "+TrainingColumns.TABLE_NAME+" ( "+TrainingColumns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TrainingColumns.TRAINING_INFO_ID+" int NOT NULL," +
                TrainingColumns.EXERCISE_NAME_ID+"  NOT NULL,"
                +TrainingColumns.DATE+" STRING NOT NULL,"
                +TrainingColumns.WEIGHT+" REAL NOT NULL,"
                +TrainingColumns.REPS+" INT NOT NULL,"
                +TrainingColumns.SERIE+" INT NOT NULL,"
                +TrainingColumns.IS_SCHEMA+" INT NOT NULL,"
                +TrainingColumns.SCHEMA+" STRING NOT NULL )");
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
