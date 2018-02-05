package com.example.daniel.database.exercise;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Daniel on 2017-04-07.
 */

public class ExerciseDatabase extends SQLiteOpenHelper {
    private static final String DATA_BASE_NAME = "treningi_dane.db";
    private static final int DATA_BASE_VERSION = 1;
    android.content.Context Context;

    public ExerciseDatabase(android.content.Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
        Context = context;
    }
    public ExerciseDatabase(android.content.Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
        Context = context;
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ExerciseColumnNames.TABLE_NAME + " ( "+ ExerciseColumnNames._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ ExerciseColumnNames.EXERCISE_NAME + " TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ ExerciseColumnNames.TABLE_NAME);
        onCreate(db);
    }

    public void dodajĆwiczenie(Exercise ćwiczenie){
        SQLiteDatabase db = this.getWritableDatabase();

        if(czyNazwaĆwiczeniaJużByła(ćwiczenie.getNazwa())) return;
        ContentValues values = new ContentValues();
        values.put(ExerciseColumnNames.EXERCISE_NAME, ćwiczenie.getNazwa());

        db.insert(ExerciseColumnNames.TABLE_NAME, null, values);
        db.close();
    }

    private boolean czyNazwaĆwiczeniaJużByła(String nazwa) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ExerciseColumnNames.TABLE_NAME, new String[] {ExerciseColumnNames._ID, ExerciseColumnNames.EXERCISE_NAME}, null,null,null,null, ExerciseColumnNames._ID);
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
    }

    public Exercise wczytajĆwiczenie(int row, int column){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ExerciseColumnNames.TABLE_NAME, new String[] {ExerciseColumnNames._ID, ExerciseColumnNames.EXERCISE_NAME}, null,null,null,null, ExerciseColumnNames._ID);
        if (cursor!=null){
            cursor.moveToPosition(row);
        }
        Log.d("cursor",cursor.toString());
        Exercise ćwiczenie = new Exercise(cursor.getString(column));
        db.close();
        return  ćwiczenie;
    }
    public Exercise[] wczytajWszystkiejĆwiczenie(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ExerciseColumnNames.TABLE_NAME, new String[] {ExerciseColumnNames._ID, ExerciseColumnNames.EXERCISE_NAME}, null,null,null,null, ExerciseColumnNames._ID);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        Exercise[] ćwiczenie = new Exercise[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++){
            ćwiczenie[i] = new Exercise(cursor.getString(1));
            cursor.moveToNext();
        }
        db.close();
        return  ćwiczenie;
    }
    public void usuńĆwiczenie(int numer){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ExerciseColumnNames.TABLE_NAME, ExerciseColumnNames._ID+" =?", new String[]{String.valueOf(numer)});
        db.close();
    }
    public void usuńWszystkie(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ExerciseColumnNames.TABLE_NAME,null,null);
    }
    public void usuńPlik(){
        Context.deleteDatabase(ExerciseColumnNames.TABLE_NAME);
    }

    public int getĆwiczeniaCount() {
        String countQuery = "SELECT  * FROM " + ExerciseColumnNames.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int wynik = cursor.getCount();
        // return count
        cursor.close();
        db.close();
        return wynik;
    }
}
