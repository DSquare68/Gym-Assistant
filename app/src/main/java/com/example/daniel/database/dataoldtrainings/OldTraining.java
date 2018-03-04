package com.example.daniel.database.dataoldtrainings;

import android.content.Context;

import com.example.daniel.database.exercise.name.ExerciseDatabase;
import com.example.daniel.database.trainings.names.TrainingNamesDatabase;

/**
 * Created by Daniel on 2017-05-19.
 */

public class OldTraining {

    private String trainingName, exerciseName, trainingDate,trainingDuration, trainingTime;
    private int  trainingID,exerciseID,roundNumber, reps;
    private double weight;


    public OldTraining(String trainingname,int trainingID, String execiseName,int exerciseID, String date, String time, String duration, int roundNumber, int reps, double weight){
        this.trainingName=trainingname;
        this.trainingID=trainingID;
        this.exerciseName=execiseName;
        this.exerciseID=exerciseID;
        this.trainingDate=date;
        this.trainingTime=time;
        this.trainingDuration=duration;
        this.roundNumber=roundNumber;
        this.reps =reps;
        this.weight=weight;
    }
    public OldTraining(int trainingID,int exerciseID,String date, String time,String duration, int roundNumber, int reps, double weight){
        this.trainingID=trainingID;
        this.exerciseID=exerciseID;
        this.trainingDate=date;
        this.trainingTime=time;
        this.trainingDuration=duration;
        this.roundNumber=roundNumber;
        this.reps =reps;
        this.weight=weight;
    }
    public OldTraining(int trainingID,int exerciseID,String date, String time,String duration, int roundNumber, int reps, double weight, Context context){
        TrainingNamesDatabase tnd = new TrainingNamesDatabase(context);
        ExerciseDatabase ed = new ExerciseDatabase(context);
        this.trainingName=tnd.getTrainingName(trainingID).getName();
        this.exerciseName=ed.getExercise(exerciseID).getName();
        this.trainingID=trainingID;
        this.exerciseID=exerciseID;
        this.trainingDate=date;
        this.trainingTime=time;
        this.trainingDuration=duration;
        this.roundNumber=roundNumber;
        this.reps =reps;
        this.weight=weight;
    }
    public String getTrainingName() {
        return trainingName;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public String getTrainingDuration() {
        return trainingDuration;
    }

    public String getTrainingTime() {
        return trainingTime;
    }

    public int getTrainingID() {
        return trainingID;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
    }

    public void setTrainingDuration(String trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public void setTrainingTime(String trainingTime) {
        this.trainingTime = trainingTime;
    }

    public void setTrainingID(int trainingID) {
        this.trainingID = trainingID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
