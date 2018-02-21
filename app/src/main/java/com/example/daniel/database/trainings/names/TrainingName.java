package com.example.daniel.database.trainings.names;

/**
 * Created by Daniel on 2017-04-22.
 */

public class TrainingName {
    private String name;
    private int ID;


    public TrainingName(String n, int ID) {

        this.name = n;
        this.ID = ID;
    }
    public TrainingName(String n) {

        this.name = n;
    }

    public String getName() {
        return name;
    }

    public void setname(String nazwa) {
        this.name = nazwa;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}





