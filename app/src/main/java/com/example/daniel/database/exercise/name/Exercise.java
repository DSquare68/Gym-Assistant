package com.example.daniel.database.exercise.name;

import android.content.Context;

/**
 * Created by Daniel on 2017-04-05.
 */

public class Exercise {
    private String name;
    private int ID;

    public Exercise(String n){
        this.name =n;
    }
    public Exercise(String n, Context context){
        ExerciseDatabase ed = new ExerciseDatabase(context);
        this.ID=ed.getIndex(n);
        this.name =n;
    }

    public void setName(String nazwa){
        this.name = nazwa;
    }
    public String getName(){
        return name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
