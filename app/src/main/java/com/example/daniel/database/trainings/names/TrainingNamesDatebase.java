package com.example.daniel.database.trainings.names;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TrainingNamesDatebase extends SQLiteOpenHelper {
    private static final String DATA_BASE_NAME = "trainings.db";
    private static final int DATA_BASE_VERSION = 1;
    Context context;

    public TrainingNamesDatebase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public TrainingNamesDatebase(Context context){
        super(context,DATA_BASE_NAME,null,DATA_BASE_VERSION);
        this.context = context;
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TrainingNamesColumns.TABLE_NAME + " ( "+TrainingNamesColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ TrainingNamesColumns.TRAINING_NAME+ " TEXT );");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TrainingNamesColumns.TABLE_NAME);
        onCreate(db);
    }
    public void addTrainingName(TrainingName ćwiczenie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TrainingNamesColumns.TRAINING_NAME, ćwiczenie.getNazwa());

        db.insert(TrainingNamesColumns.TABLE_NAME, null, values);
        db.close();
    }

    public void addTrainingName(String training){
        SQLiteDatabase db = this.getWritableDatabase();

        //if(czyNazwaĆwiczeniaJużByła(ćwiczenie.getNazwa())) return;
        ContentValues values = new ContentValues();
        values.put(TrainingNamesColumns.TRAINING_NAME, training);

        db.insert(TrainingNamesColumns.TABLE_NAME, null, values);
        db.close();
    }

    private boolean isTrainingNameRepeated(String trainingName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TrainingNamesColumns.TABLE_NAME, new String[] {TrainingNamesColumns._ID, TrainingNamesColumns.TRAINING_NAME}, null,null,null,null,TrainingNamesColumns._ID);
        int ilość = cursor.getCount();
        cursor.moveToFirst();
        for(int i=0;i<ilość;i++){
            if(trainingName.equals(cursor.getString(1))){
                db.close();
                return true;
            } else{
                cursor.moveToNext();
            }
        }
        db.close();
        return false;
    }

    public TrainingName readTrainingName(int row, int column){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TrainingNamesColumns.TABLE_NAME, new String[] {TrainingNamesColumns._ID, TrainingNamesColumns.TRAINING_NAME}, null,null,null,null,TrainingNamesColumns._ID);
        if (cursor!=null){
            cursor.moveToPosition(row);
        }
        TrainingName ćwiczenie = new TrainingName(cursor.getString(column));
        db.close();
        return  ćwiczenie;
    }

    public TrainingName[] readAllTrainings(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TrainingNamesColumns.TABLE_NAME, new String[] {TrainingNamesColumns._ID, TrainingNamesColumns.TRAINING_NAME}, null,null,null,null,TrainingNamesColumns._ID);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        TrainingName[] ćwiczenie = new TrainingName[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++){
            ćwiczenie[i] = new TrainingName(cursor.getString(1));
            cursor.moveToNext();
        }
        db.close();
        return  ćwiczenie;
    }

    public void deleteTrainingName(int numer){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TrainingNamesColumns.TABLE_NAME, TrainingNamesColumns._ID+" =?", new String[]{String.valueOf(numer)});
        db.close();
    }
    public void deleteTrainingName(String nazwaTreningu){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TrainingNamesColumns.TABLE_NAME, TrainingNamesColumns.TABLE_NAME+" =?", new String[]{nazwaTreningu});
        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TrainingNamesColumns.TABLE_NAME,null,null);
        db.close();
    }
    public void deleteDb(Context context){
        context.deleteDatabase(TrainingNamesColumns.TABLE_NAME+".db");
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + TrainingNamesColumns.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();

    }
}
