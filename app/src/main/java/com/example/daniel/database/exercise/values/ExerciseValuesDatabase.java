package com.example.daniel.database.exercise.values;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.daniel.database.exercise.name.Exercise;


public class ExerciseValuesDatabase extends SQLiteOpenHelper {
    private static final String DATA_BASE_NAME = "trainings.db";
    private static final int DATA_BASE_VERSION = 1;

    public ExerciseValuesDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public ExerciseValuesDatabase(Context context){

        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ExerciseValuesColumns.TABLE_NAME + " ( "+ ExerciseValuesColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ ExerciseValuesColumns.EXERCISE_ID+" TEXT, "+ ExerciseValuesColumns.TRANING_ID+" TEXT, "+ ExerciseValuesColumns.EXERCISE_NUMBER+" INTEGER, "+ ExerciseValuesColumns.ROUND_NUMBER+" INTEGER, "+ ExerciseValuesColumns.WEIGHT+" NUMBER, "+ ExerciseValuesColumns.REPS+" INTEGER );");
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
        values.put(ExerciseValuesColumns.TRANING_ID, exercise.getTrainingID());
        values.put(ExerciseValuesColumns.EXERCISE_NUMBER, exercise.getRoundNumber());
        values.put(ExerciseValuesColumns.ROUND_NUMBER, exercise.getRoundNumber());
        values.put(ExerciseValuesColumns.WEIGHT, exercise.getWeight());
        values.put(ExerciseValuesColumns.REPS, exercise.getReps());


        db.insert(ExerciseValuesColumns.TABLE_NAME, null, values);
        db.close();
    }

    /**
     * @param exercise w nim ma być zawarty nowy, jeśli się zmienił, numer cwicznia
     *
     */
    public void editExerciseValue(ExerciseValue exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ExerciseValuesColumns.TABLE_NAME,ExerciseValuesColumns.TRANING_ID+" = ?",new String[]{String.valueOf(exercise.getTrainingID())});
        ContentValues values = new ContentValues();
        values.put(ExerciseValuesColumns.EXERCISE_ID, exercise.getNameID());
        values.put(ExerciseValuesColumns.TRANING_ID, exercise.getTrainingID());
        values.put(ExerciseValuesColumns.EXERCISE_NUMBER, exercise.getRoundNumber());
        values.put(ExerciseValuesColumns.ROUND_NUMBER, exercise.getRoundNumber());
        values.put(ExerciseValuesColumns.WEIGHT, exercise.getWeight());
        values.put(ExerciseValuesColumns.REPS, exercise.getReps());


        db.insert(ExerciseValuesColumns.TABLE_NAME,  null,values);
        db.close();
    }

    /*private boolean czyNazwaĆwiczeniaJużByła(String nazwa) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ExerciseValuesColumns.TABLE_NAME, new String[] {ExerciseValuesColumns._ID, ExerciseValuesColumns.NAZWA_ĆWICZENIA, ExerciseValuesColumns.ILOŚĆ_SERII, ExerciseValuesColumns.OBCIĄŻENIE, ExerciseValuesColumns.ILOŚĆ_POWTÓRZEŃ}, null,null,null,null,ExerciseValuesColumns._ID);
        int ilość = cursor.getCount();
        cursor.moveToFirst();
        for(int i=0;i<ilość;i++){
            if(nazwa.equals(cursor.getString(1))){
                db.close();
                return true;
            } else{
                cursor.moveToNext();
            }
        }
        return false;
    }*/

    public ExerciseValue get(int row){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ExerciseValuesColumns.TABLE_NAME, new String[] {ExerciseValuesColumns._ID, ExerciseValuesColumns.EXERCISE_ID, ExerciseValuesColumns.TRANING_ID,ExerciseValuesColumns.EXERCISE_NUMBER,ExerciseValuesColumns.ROUND_NUMBER, ExerciseValuesColumns.WEIGHT, ExerciseValuesColumns.REPS}, null,null,null,null,ExerciseValuesColumns._ID);
        if (cursor!=null){
            cursor.moveToPosition(row);
        }
        ExerciseValue ćwiczenie = new ExerciseValue(cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4), cursor.getDouble(5),cursor.getInt(6));
        db.close();
        return  ćwiczenie;
    }
    public Exercise[] getAllArderedByID(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT "+ExerciseValuesColumns.EXERCISE_ID+" FROM "+ExerciseValuesColumns.TABLE_NAME+" ORDER BY "+ExerciseValuesColumns.EXERCISE_ID+" DESC;";
        Cursor cursor= db.rawQuery(query, new String[]{});
        cursor.moveToFirst();
        Exercise[] wynik= new Exercise[cursor.getCount()];
        if(cursor.getCount()==0) return null;
        for(int i=0;i<cursor.getCount();i++,cursor.moveToNext()){
            wynik[i] = new Exercise(cursor.getString(0));
        }
        return   wynik;
    }
    public ExerciseValue[] get(String nazwaTreningu){
        SQLiteDatabase db = this.getReadableDatabase();
        //String query ="SELECT * FROM "+ExerciseValuesColumns.TABLE_NAME+" WHERE "+ ExerciseValuesColumns.NAZWA_TRENINGU +" = " + nazwaTreningu + " ORDER BY "+ExerciseValuesColumns.NAZWA_ĆWICZENIA;
        Cursor cursor = db.query(ExerciseValuesColumns.TABLE_NAME, new String[] {ExerciseValuesColumns._ID, ExerciseValuesColumns.EXERCISE_ID, ExerciseValuesColumns.TRANING_ID, ExerciseValuesColumns.EXERCISE_NUMBER,ExerciseValuesColumns.ROUND_NUMBER, ExerciseValuesColumns.WEIGHT, ExerciseValuesColumns.REPS}, ExerciseValuesColumns.TRANING_ID+" = '"+nazwaTreningu+"'",null,null,null,ExerciseValuesColumns.EXERCISE_NUMBER);
        //Cursor cursor = db.rawQuery(query,null);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        Log.d("cursor ", String.valueOf(cursor.getCount()));
        ExerciseValue[] ćwiczenie = new ExerciseValue[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++){
            ćwiczenie[i] = new ExerciseValue(cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4), cursor.getDouble(5),cursor.getInt(6));
            cursor.moveToNext();
        }
        db.close();
        return  ćwiczenie;
    }
    public ExerciseValue[] getAll(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ExerciseValuesColumns.TABLE_NAME, new String[] {ExerciseValuesColumns._ID, ExerciseValuesColumns.EXERCISE_ID,ExerciseValuesColumns.TRANING_ID, ExerciseValuesColumns.ROUND_NUMBER, ExerciseValuesColumns.WEIGHT, ExerciseValuesColumns.REPS}, null,null,null,null,ExerciseValuesColumns._ID);
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
    public void delate(int numer){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ExerciseValuesColumns.TABLE_NAME, ExerciseValuesColumns._ID+" =?", new String[]{String.valueOf(numer)});
        db.close();
    }
    public void delate(String nazwa){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ExerciseValuesColumns.TABLE_NAME, ExerciseValuesColumns.TRANING_ID+" =?", new String[]{nazwa});
        db.close();
    }
    public void delateAll(){
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
