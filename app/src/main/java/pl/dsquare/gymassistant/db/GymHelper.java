package pl.dsquare.gymassistant.db;

import static pl.dsquare.gymassistant.db.ExerciseColumns.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GymHelper extends SQLiteOpenHelper {

    SQLiteDatabase sql;

    public GymHelper(@Nullable @org.jetbrains.annotations.Nullable Context context, @Nullable @org.jetbrains.annotations.Nullable String name, @Nullable @org.jetbrains.annotations.Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB.EXERCISE_DB_NAME, factory, DB.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sql=sqLiteDatabase;
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+" ("+ID+" integer primary key,"+EXERCISE_NAME+" text )");
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+" ("+ID+" integer primary key,"+EXERCISE_NAME+" text )");
        initExerciseNames();
    }

    private void initExerciseNames() {
        ContentValues cv = new ContentValues();
        for(int i=0; i<ExerciseNames.nameTrainings.length;i++)
            cv.put(EXERCISE_NAME,ExerciseNames.nameTrainings[i]);
        sql.insert(TABLE_NAME,null,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
