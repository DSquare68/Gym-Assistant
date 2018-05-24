package com.example.daniel.database.dataoldtrainings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.daniel.database.trainings.names.TrainingName;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValuesDatabase;


/**
 * Created by Daniel on 2017-05-19.
 */

public class OldTrainingsDatabase extends SQLiteOpenHelper{

    private static final String EXTENSION_DB = "old_trainings.db";

    private static final String COLUMNS_INIT = " ( "+ OldTrainingsColumnNames._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ OldTrainingsColumnNames.TRAINING_ID+" INTEGER, "
            +OldTrainingsColumnNames.EXERCISE_ID+" TEXT, "+OldTrainingsColumnNames.DATE+" TEXT, "+ OldTrainingsColumnNames.TIME+" TEXT, "+OldTrainingsColumnNames.DURATION+" TEXT, "
            + OldTrainingsColumnNames.ROUND_NUMBER+" INTEGER, "+ OldTrainingsColumnNames.REPS+" INTEGER, "+ OldTrainingsColumnNames.WEIGHT+" INTEGER "+");";

    private static final String COLUMNS = " ( "+ OldTrainingsColumnNames._ID+", "+ OldTrainingsColumnNames.TRAINING_ID+", "
            +OldTrainingsColumnNames.EXERCISE_ID+", "+OldTrainingsColumnNames.DATE+", "+ OldTrainingsColumnNames.TIME+", "+OldTrainingsColumnNames.DURATION+", "
            + OldTrainingsColumnNames.ROUND_NUMBER+", "+ OldTrainingsColumnNames.REPS+", "+ OldTrainingsColumnNames.WEIGHT+" );";



    private static final int DATA_BASE_VERSION = 1;
    String TABLE_NAME = null;
    Context context;

    public OldTrainingsDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public OldTrainingsDatabase(Context context, String name){
        super(context, EXTENSION_DB, null, DATA_BASE_VERSION);
        TABLE_NAME=name;
        createUserTable(TABLE_NAME);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + "["+TABLE_NAME+"]" + COLUMNS_INIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ["+ TABLE_NAME+"]");
        onCreate(db);
    }
    public void createUserTable(String user) {
        if(checkIfTabExists(user)) {
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        String CREATE_TABLE_NEW_USER = "CREATE TABLE " +"["+user+"]" + COLUMNS_INIT;
        db.execSQL(CREATE_TABLE_NEW_USER);
        db.close();
    }
    public boolean checkIfTabExists(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if(name==null) return true;
        if (c.moveToFirst()) {
            while ( c.moveToNext() ) {
               if(name.equals(c.getString(0))) {
                    return true;
                }
            }
        }
        return false;
    }
    public void addOldTraining(OldTraining exercise){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OldTrainingsColumnNames.TRAINING_ID,exercise.getTrainingID());
        values.put(OldTrainingsColumnNames.EXERCISE_ID, exercise.getExerciseID());
        values.put(OldTrainingsColumnNames.DATE,exercise.getTrainingDate());
        values.put(OldTrainingsColumnNames.TIME,exercise.getTrainingTime());
        values.put(OldTrainingsColumnNames.DURATION,exercise.getTrainingDuration());
        values.put(OldTrainingsColumnNames.ROUND_NUMBER, exercise.getRoundNumber());
        values.put(OldTrainingsColumnNames.REPS, exercise.getReps());
        values.put(OldTrainingsColumnNames.WEIGHT,exercise.getWeight());




        db.insert("["+TABLE_NAME+"]", null, values);
        db.close();
    }
    public OldTraining[] getOldTrainings(int exerciseID){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("["+TABLE_NAME+"]", new String[] {OldTrainingsColumnNames._ID,OldTrainingsColumnNames.TRAINING_ID,OldTrainingsColumnNames.EXERCISE_ID,
                OldTrainingsColumnNames.DATE,OldTrainingsColumnNames.TIME,OldTrainingsColumnNames.DURATION,OldTrainingsColumnNames.ROUND_NUMBER,
                OldTrainingsColumnNames.REPS, OldTrainingsColumnNames.WEIGHT}, OldTrainingsColumnNames.EXERCISE_ID +" = '"+exerciseID+"' ",null,null,null,OldTrainingsColumnNames._ID);
        cursor.moveToFirst();
        OldTraining[] old= new OldTraining[cursor.getCount()];
        int i=0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext(),i++) {
             old[i]= new OldTraining(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4),cursor.getString(5), cursor.getInt(6), cursor.getInt(7),cursor.getDouble(8));
        }
        db.close();
        return  old;
    }
    public OldTraining[] getOldTraining(String trainingID){
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT "+ COLUMNS + "FROM "+"["+TABLE_NAME+"] WHERE "+OldTrainingsColumnNames.TRAINING_ID+"= ["+trainingID+"] GROUP BY "+OldTrainingsColumnNames.EXERCISE_ID;
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        OldTraining[] oldTrainings= new OldTraining[cursor.getCount()];
        int i=0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext(),i++) {
            oldTrainings[i]= new OldTraining(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7),cursor.getDouble(8));
        }
        db.close();
        return  oldTrainings;
    }
    public String[][] getDateAndTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, " ["+TABLE_NAME+"]", new String[] { OldTrainingsColumnNames.DATE, OldTrainingsColumnNames.DURATION,OldTrainingsColumnNames.TIME}, null, null, null, null, OldTrainingsColumnNames.DATE+", "+OldTrainingsColumnNames.TIME+" ASC", null);
        if (cursor.getCount()==0) return null;
        String[][] dataAndTime = new String[cursor.getCount()][3];
        int i=0;
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext(),i++){
            dataAndTime[i][0] = cursor.getString(0);
            dataAndTime[i][1] = cursor.getString(1);
            dataAndTime[i][2] = cursor.getString(2);
        }
        db.close();
        return dataAndTime;
    }
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        if (TABLE_NAME==null){
           Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);
           cursor.moveToFirst();
           while (cursor.moveToNext()){
               db.delete("["+cursor.getString(0)+"]", null, null);
           }
           return;
        }
        db.delete("["+TABLE_NAME+"]", null, null);
        db.close();
    }
    public void deleteFile(Context context, String name){
        close();
        context.deleteDatabase(name+EXTENSION_DB);
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //db.close();
        // return count
        return cursor.getCount();
    }

    public OldTraining[][] getAll(String s, String s1) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("["+TABLE_NAME+"]", new String[] {OldTrainingsColumnNames._ID,OldTrainingsColumnNames.TRAINING_ID,OldTrainingsColumnNames.EXERCISE_ID, OldTrainingsColumnNames.DATE,OldTrainingsColumnNames.TIME,OldTrainingsColumnNames.DURATION,OldTrainingsColumnNames.ROUND_NUMBER,OldTrainingsColumnNames.REPS, OldTrainingsColumnNames.WEIGHT}, OldTrainingsColumnNames.DATE +" = '"+s+"' AND "+OldTrainingsColumnNames.TIME +" = '"+s1+"' ",null,null,null,OldTrainingsColumnNames._ID);
        TrainingValuesDatabase wtd = new TrainingValuesDatabase(context);
        cursor.moveToFirst();
        TrainingValue trainingValue =wtd.getByTrainingID(cursor.getInt(1));
        cursor.moveToFirst();
        int maxRounds=1, numberOfExercise=0,round=0;
        for(int i=0; i<cursor.getCount();cursor.moveToNext(),i++){

            if(cursor.getInt(6)==1) {
                if (round>maxRounds){
                    maxRounds=round;
                }
                round=1;
                numberOfExercise++;

            } else{
                round++;
            }
            if(cursor.isLast()){
                if (round>maxRounds){
                    maxRounds=round;
                }
            }

        }
        OldTraining[][] oldTrainings= new OldTraining[numberOfExercise][maxRounds];
        int i=-1,j=0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if(cursor.getInt(6)==1) {j=0; i++;}
            if(j>=maxRounds) {j=0; i++;}
            if(i>=trainingValue.getExerciseNumber()) {db.close(); return oldTrainings;}
            oldTrainings[i][j++] = new OldTraining(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7), cursor.getDouble(8));
        }
        db.close();
        return oldTrainings;
    }
    public int[] getRoundsNumber(String data, String time){
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT "+OldTrainingsColumnNames.EXERCISE_ID+", MAX( "+OldTrainingsColumnNames.ROUND_NUMBER+" ) FROM "+"["+TABLE_NAME+"]"+" WHERE "+OldTrainingsColumnNames.DATE+" = '"+data+"' AND "+OldTrainingsColumnNames.TIME+"= '"+time+"' GROUP BY "+OldTrainingsColumnNames.EXERCISE_ID+" ORDER BY +"+OldTrainingsColumnNames.EXERCISE_ID+" DESC";
        Cursor cursor = db.rawQuery(query,null);
        int i=0;
        int[] count= new int[cursor.getCount()];
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext(),i++){
            count[i]=cursor.getInt(1);
        }
        db.close();
        return count;
    }
    public OldTraining[][] getTrainingFromTimeAndDate(String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("["+TABLE_NAME+"]", new String[] {OldTrainingsColumnNames._ID,OldTrainingsColumnNames.TRAINING_ID,OldTrainingsColumnNames.EXERCISE_ID, OldTrainingsColumnNames.DATE,OldTrainingsColumnNames.TIME,OldTrainingsColumnNames.DURATION,OldTrainingsColumnNames.ROUND_NUMBER,OldTrainingsColumnNames.REPS, OldTrainingsColumnNames.WEIGHT}, OldTrainingsColumnNames.DATE +" = '"+date+"' AND "+OldTrainingsColumnNames.TIME +" = '"+time+"' ",null,null,null,OldTrainingsColumnNames._ID);
        TrainingValuesDatabase wtd = new TrainingValuesDatabase(context);
        cursor.moveToFirst();
        TrainingValue trainingValue =wtd.getByTrainingID(cursor.getInt(1));
        cursor.moveToFirst();
        int maxRounds=1, numberOfExercise=0,round=0;
        for(int i=0; i<cursor.getCount();cursor.moveToNext(),i++){

            if(cursor.getInt(6)==1) {
                if (round>maxRounds){
                    maxRounds=round;
                }
                round=1;
                numberOfExercise++;

            } else{
                round++;
            }
            if(cursor.isLast()){
                if (round>maxRounds){
                    maxRounds=round;
                }
            }

        }
        OldTraining[][] oldTrainings= new OldTraining[numberOfExercise][maxRounds];
        int i=-1,j=0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if(cursor.getInt(6)==1) {j=0; i++;}
            if(j>=maxRounds) {j=0; i++;}
            if(i>=trainingValue.getExerciseNumber()) {db.close(); return oldTrainings;}
            oldTrainings[i][j++] = new OldTraining(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7), cursor.getDouble(8),context);
        }
        db.close();
        return oldTrainings;
    }
    public OldTraining[] get(int trainingID, int exerciseID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM '"+TABLE_NAME+"' WHERE "+OldTrainingsColumnNames.TRAINING_ID+" = ? AND "+ OldTrainingsColumnNames.EXERCISE_ID+" = ? ORDER BY "+OldTrainingsColumnNames.DATE+", "+OldTrainingsColumnNames.TIME+", "+OldTrainingsColumnNames.ROUND_NUMBER+";";
        Cursor cursor = db.rawQuery(query,new String[] {String.valueOf(trainingID),String.valueOf(exerciseID)});
        OldTraining[] oldTrainings =new OldTraining[cursor.getCount()];
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++,cursor.moveToNext()){
            oldTrainings[i]=new OldTraining(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getInt(7), cursor.getDouble(8),context);
        }
        return oldTrainings;
    }

    public int maxNumberOfRounds(int trainingID, int exerciseID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX ("+OldTrainingsColumnNames.ROUND_NUMBER+") FROM '"+TABLE_NAME+"' WHERE "+OldTrainingsColumnNames.TRAINING_ID+" = ? AND "+ OldTrainingsColumnNames.EXERCISE_ID+" = ? ORDER BY "+OldTrainingsColumnNames.DATE+";";
        Cursor cursor = db.rawQuery(query,new String[] {String.valueOf(trainingID),String.valueOf(exerciseID)});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public String[] getDataTrainings(int exerciseID){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT "+OldTrainingsColumnNames.DATE+" FROM '"+TABLE_NAME+"' WHERE "+OldTrainingsColumnNames.EXERCISE_ID+" = ? ORDER BY "+OldTrainingsColumnNames.DATE+";";
        Cursor cursor = db.rawQuery(query,new String[] {String.valueOf(exerciseID)});
        String[] result =new String[cursor.getCount()];
        if (cursor.getCount()<=0) {
            for(int i=0;i<cursor.getCount();i++,cursor.moveToNext()){
                result[i]="";
            }
            return result;
        }
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++,cursor.moveToNext()){
            result[i]=(cursor.getString(0));
        }
        return result;
    }

    public String[] getTime(int exerciseID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT "+ OldTrainingsColumnNames.TIME+" FROM '"+TABLE_NAME+"' WHERE "+ OldTrainingsColumnNames.EXERCISE_ID+" = ? ORDER BY "+ OldTrainingsColumnNames.DATE+";";
        Cursor cursor = db.rawQuery(query,new String[] {String.valueOf(exerciseID)});
        String[] result =new String[cursor.getCount()];
        if (cursor.getCount()<=0) {
            for(int i=0;i<cursor.getCount();i++,cursor.moveToNext()){
                result[i]="";
            }
            return result;
        }
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++,cursor.moveToNext()){
            result[i]=(cursor.getString(0));
        }
        return result;
    }
    public String getDB() {
        return context.getDatabasePath("old_trainings.db").toString();
    }
}
