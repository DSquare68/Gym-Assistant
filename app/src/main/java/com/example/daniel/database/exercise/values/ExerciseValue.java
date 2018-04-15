package com.example.daniel.database.exercise.values;

import android.content.Context;

import com.example.daniel.database.exercise.name.ExerciseDatabase;
import com.example.daniel.database.trainings.names.TrainingNamesDatabase;

/**
 * Created by Daniel on 2017-04-22.
 */

public class ExerciseValue {
    private String name, trainingName;
    private int nameID, trainingID, roundNumber, reps, exerciseNumber;
    private double weight;
    public ExerciseValue(){

    }

    public ExerciseValue(String name,int nameID, String trainingName, int trainingID, int exerciseNumber, int roundNumber, double weight, int reps){
        this.name=name;
        this.nameID=nameID;
        this.trainingName =trainingName;
        this.trainingID=trainingID;
        this.exerciseNumber =exerciseNumber;
        this.roundNumber =roundNumber;
        this.weight =weight;
        this.reps =reps;
    }
    public ExerciseValue(int nameID, int trainingID, int exerciseNumber, int roundNumber, double weight, int reps){
        this.nameID=nameID;
        this.trainingID =trainingID;
        this.exerciseNumber =exerciseNumber;
        this.roundNumber =roundNumber;
        this.weight =weight;
        this.reps =reps;
    }
    public ExerciseValue(int nameID, int trainingID, int exerciseNumber, int roundNumber, double weight, int reps, Context context){
        ExerciseDatabase exerciseDatabase = new ExerciseDatabase(context);
        TrainingNamesDatabase trainingNamesDatabase = new TrainingNamesDatabase(context);
        this.name=exerciseDatabase.getExercise(nameID).getName();
        this.trainingName=trainingNamesDatabase.getTrainingName(trainingID).getName();
        this.nameID=nameID;
        this.trainingID =trainingID;
        this.exerciseNumber =exerciseNumber;
        this.roundNumber =roundNumber;
        this.weight =weight;
        this.reps =reps;
    }
    public String getName() {
        return name;
    }

    public int getNameID() {
        return nameID;
    }

    public int getTrainingID() {
        return trainingID;
    }

    public void setNameID(int nameID) {
        this.nameID = nameID;
    }

    public void setTrainingID(int trainingID) {
        this.trainingID = trainingID;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getReps() {
        return reps;
    }

    public int getExerciseNumber() {
        return exerciseNumber;
    }

    public double getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setExerciseNumber(int exerciseNumber) {
        this.exerciseNumber = exerciseNumber;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}




