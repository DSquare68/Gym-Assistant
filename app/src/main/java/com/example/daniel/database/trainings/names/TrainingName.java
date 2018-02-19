package com.example.daniel.database.trainings.names;

/**
 * Created by Daniel on 2017-04-22.
 */

public class TrainingName {
    private String nazwa;
    private int ID;


    public TrainingName(String n, int ID) {

        this.nazwa = n;
        this.ID = ID;
    }
    public TrainingName(String n) {

        this.nazwa = n;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}





