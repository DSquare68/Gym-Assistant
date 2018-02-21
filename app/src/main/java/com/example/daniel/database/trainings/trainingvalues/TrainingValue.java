package com.example.daniel.database.trainings.trainingvalues;

import android.content.Context;

import com.example.daniel.database.trainings.names.TrainingNamesDatabase;

/**
 * Created by Daniel on 2017-04-25.
 */

public class TrainingValue {
    private long averageTime;
    private int trainingId=-1, roundsNumber, exerciseNumber, repetition;
    private String trainingName, weekDays, trainingMode, schedule, addDate, firstDayTraining, lastTrainingDayDate;

    public TrainingValue(String treningName, int trainingID, String weekDays, String trainingMode, String schedule, int roundsNumber, int exerciseNumber, String addDate, String firstDayTraining, String lastTrainingDayDate, int repetition, long averageTime) {
        this.trainingName = treningName;
        this.weekDays = weekDays;
        this.trainingMode = trainingMode;
        this.schedule = schedule;
        this.roundsNumber = roundsNumber;
        this.exerciseNumber = exerciseNumber;
        this.addDate = addDate;
        this.firstDayTraining = firstDayTraining;
        this.lastTrainingDayDate = lastTrainingDayDate;
        this.repetition = repetition;
        this.averageTime = averageTime;
    }

    public TrainingValue(int trainingID, String weekDays, String trainingMode, String schedule, int roundsNumber, int exerciseNumber, String addDate, String firstDayTraining, String lastTrainingDayDate, int repetition, long averageTime) {
        this.trainingId = trainingID;
        this.weekDays = weekDays;
        this.trainingMode = trainingMode;
        this.schedule = schedule;
        this.roundsNumber = roundsNumber;
        this.exerciseNumber = exerciseNumber;
        this.addDate = addDate;
        this.firstDayTraining = firstDayTraining;
        this.lastTrainingDayDate = lastTrainingDayDate;
        this.repetition = repetition;
        this.averageTime = averageTime;
    }
    public TrainingValue(int trainingID, String weekDays, String trainingMode, String schedule, int roundsNumber, int exerciseNumber, String addDate, String firstDayTraining, String lastTrainingDayDate, int repetition, long averageTime, Context context) {
        TrainingNamesDatabase trainingNamesDatabase = new TrainingNamesDatabase(context);
        this.trainingName =trainingNamesDatabase.getTrainingName(trainingID).getName();
        this.trainingId = trainingID;
        this.weekDays = weekDays;
        this.trainingMode = trainingMode;
        this.schedule = schedule;
        this.roundsNumber = roundsNumber;
        this.exerciseNumber = exerciseNumber;
        this.addDate = addDate;
        this.firstDayTraining = firstDayTraining;
        this.lastTrainingDayDate = lastTrainingDayDate;
        this.repetition = repetition;
        this.averageTime = averageTime;
    }

    public long getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(long averageTime) {
        this.averageTime = averageTime;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public int getRoundsNumber() {
        return roundsNumber;
    }

    public void setRoundsNumber(int roundsNumber) {
        this.roundsNumber = roundsNumber;
    }

    public int getExerciseNumber() {
        return exerciseNumber;
    }

    public void setExerciseNumber(int exerciseNumber) {
        this.exerciseNumber = exerciseNumber;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    public String getTrainingMode() {
        return trainingMode;
    }

    public void setTrainingMode(String trainingMode) {
        this.trainingMode = trainingMode;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getFirstDayTraining() {
        return firstDayTraining;
    }

    public void setFirstDayTraining(String firstDayTraining) {
        this.firstDayTraining = firstDayTraining;
    }

    public String getLastTrainingDayDate() {
        return lastTrainingDayDate;
    }

    public void setLastTrainingDayDate(String lastTrainingDayDate) {
        this.lastTrainingDayDate = lastTrainingDayDate;
    }
}

