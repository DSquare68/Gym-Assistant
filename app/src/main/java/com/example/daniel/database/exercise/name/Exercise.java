package com.example.daniel.database.exercise.name;

/**
 * Created by Daniel on 2017-04-05.
 */

public class Exercise {
    private String nazwa;
    private int ID;

    public Exercise(String n){
        this.nazwa=n;
    }

    public void setName(String nazwa){
        this.nazwa = nazwa;
    }
    public String getName(){
        return nazwa;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
