package com.example.daniel.database.trainings.names;

/**
 * Created by Daniel on 2017-04-22.
 */

public class TrainingName {
    private String nazwa;
    public TrainingName(String n){
        this.nazwa=n;
    }

    public  String getNazwa(){
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }
}

