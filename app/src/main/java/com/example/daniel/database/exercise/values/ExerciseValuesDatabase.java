package com.example.daniel.database.exercise.values;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.daniel.database.exercise.name.Exercise;


public class ExerciseValuesDatabase extends SQLiteOpenHelper {
    private static final String DATA_BASE_NAME = "trainings.db";
    private static final int DATA_BASE_VERSION = 2;
    private Context context;
    public ExerciseValuesDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }
    public ExerciseValuesDatabase(Context context){

        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ExerciseValuesColumns.TABLE_NAME + " ( "+ ExerciseValuesColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ ExerciseValuesColumns.EXERCISE_ID+" TEXT, "+ ExerciseValuesColumns.TRAINING_ID +" TEXT, "+ ExerciseValuesColumns.EXERCISE_NUMBER+" INTEGER, "+ ExerciseValuesColumns.ROUND_NUMBER+" INTEGER, "+ ExerciseValuesColumns.WEIGHT+" NUMBER, "+ ExerciseValuesColumns.REPS+" INTEGER );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ ExerciseValuesColumns.TABLE_NAME);
        onCreate(db);
    }

    public void addExerciseValue(ExerciseValue exercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ExerciseValuesColumns.EXERCISE_ID, exercise.getNameID());
        values.put(ExerciseValuesColumns.TRAINING_ID, exercise.getTrainingID());
        values.put(ExerciseValuesColumns.EXERCISE_NUMBER, exercise.getExerciseNumber());
        values.put(ExerciseValuesColumns.ROUND_NUMBER, exercise.getRoundNumber());
        values.put(ExerciseValuesColumns.WEIGHT, exercise.getWeight());
        values.put(ExerciseValuesColumns.REPS, exercise.getReps());


        db.insert(ExerciseValuesColumns.TABLE_NAME, null, values);
        db.close();
    }


    public void editExerciseValue(ExerciseValue exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ExerciseValuesColumns.TABLE_NAME,ExerciseValuesColumns.TRAINING_ID +" = ?",new String[]{String.valueOf(exercise.getTrainingID())});
        ContentValues values = new ContentValues();
        values.put(ExerciseValuesColumns.EXERCISE_ID, exercise.getNameID());
        values.put(ExerciseValuesColumns.TRAINING_ID, exercise.getTrainingID());
        values.put(ExerciseValuesColumns.EXERCISE_NUMBER, exercise.getRoundNumber());
        values.put(ExerciseValuesColumns.ROUND_NUMBER, exercise.getRoundNumber());
        values.put(ExerciseValuesColumns.WEIGHT, exercise.getWeight());
        values.put(ExerciseValuesColumns.REPS, exercise.getReps());


        db.insert(ExerciseValuesColumns.TABLE_NAME,  null,values);
        db.close();
    }

    public ExerciseValue get(int row){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ExerciseValuesColumns.TABLE_NAME, new String[] {ExerciseValuesColumns._ID, ExerciseValuesColumns.EXERCISE_ID, ExerciseValuesColumns.TRAINING_ID,ExerciseValuesColumns.EXERCISE_NUMBER,ExerciseValuesColumns.ROUND_NUMBER, ExerciseValuesColumns.WEIGHT, ExerciseValuesColumns.REPS}, null,null,null,null,ExerciseValuesColumns._ID);
        if (cursor!=null){
            cursor.moveToPosition(row);
        }
        ExerciseValue exercise = new ExerciseValue(cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4), cursor.getDouble(5),cursor.getInt(6));
        db.close();
        return  exercise;
    }
    public ExerciseValue[] get(String trainingName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ExerciseValuesColumns.TABLE_NAME, new String[] {ExerciseValuesColumns._ID, ExerciseValuesColumns.EXERCISE_ID, ExerciseValuesColumns.TRAINING_ID, ExerciseValuesColumns.EXERCISE_NUMBER,ExerciseValuesColumns.ROUND_NUMBER, ExerciseValuesColumns.WEIGHT, ExerciseValuesColumns.REPS}, ExerciseValuesColumns.TRAINING_ID +" = '"+trainingName+"'",null,null,null,ExerciseValuesColumns.EXERCISE_NUMBER);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        ExerciseValue[] exerciseValues = new ExerciseValue[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++){
            exerciseValues[i] = new ExerciseValue(cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4), cursor.getDouble(5),cursor.getInt(6));
            cursor.moveToNext();
        }
        db.close();
        return  exerciseValues;
    }
    public ExerciseValue[] getByID(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ExerciseValuesColumns.TABLE_NAME, new String[] {ExerciseValuesColumns._ID, ExerciseValuesColumns.EXERCISE_ID, ExerciseValuesColumns.TRAINING_ID, ExerciseValuesColumns.EXERCISE_NUMBER,ExerciseValuesColumns.ROUND_NUMBER, ExerciseValuesColumns.WEIGHT, ExerciseValuesColumns.REPS}, ExerciseValuesColumns.TRAINING_ID +" = '"+ID+"'",null,null,null,ExerciseValuesColumns.EXERCISE_NUMBER+", "+ExerciseValuesColumns.ROUND_NUMBER);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        ExerciseValue[] exerciseValues = new ExerciseValue[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++){
            exerciseValues[i] = new ExerciseValue(cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4), cursor.getDouble(5),cursor.getInt(6),context);
            cursor.moveToNext();
        }
        db.close();
        return  exerciseValues;
    }
    public ExerciseValue[] getAll(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ExerciseValuesColumns.TABLE_NAME, new String[] {ExerciseValuesColumns._ID, ExerciseValuesColumns.EXERCISE_ID,ExerciseValuesColumns.TRAINING_ID, ExerciseValuesColumns.ROUND_NUMBER, ExerciseValuesColumns.WEIGHT, ExerciseValuesColumns.REPS}, null,null,null,null,ExerciseValuesColumns._ID);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        ExerciseValue[] exercise = new ExerciseValue[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++){
            exercise[i] = new ExerciseValue(cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4), cursor.getDouble(5),cursor.getInt(6));
            cursor.moveToNext();
        }
        db.close();
        return  exercise;
    }
    public void delete(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ExerciseValuesColumns.TABLE_NAME, ExerciseValuesColumns._ID+" =?", new String[]{String.valueOf(ID)});
        db.close();
    }
    public void delete(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ExerciseValuesColumns.TABLE_NAME, ExerciseValuesColumns.TRAINING_ID +" =?", new String[]{name});
        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ExerciseValuesColumns.TABLE_NAME,null,null);
        db.close();
    }
    public void deleteDb(Context context){
        context.deleteDatabase(ExerciseValuesColumns.TABLE_NAME+".db");
    }
    public int getCount() {
        String countQuery = "SELECT  * FROM " + ExerciseValuesColumns.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //db.close();
        // return count
        return cursor.getCount();
    }
}
