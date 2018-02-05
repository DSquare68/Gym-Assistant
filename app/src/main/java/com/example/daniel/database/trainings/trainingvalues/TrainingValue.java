package com.example.daniel.database.trainings.trainingvalues;

/**
 * Created by Daniel on 2017-04-25.
 */

public class TrainingValue {
    private long averageTime;
    private int trainingId, roundsNumber, exerciseNumber, repetition;
    private String traningName, weekDays, trainingMode, shedshule, addDate, firstDayTraining, lastTrainingDayDate;

    public TrainingValue(String treningName, int trainingID, String weekDays, String trainingMode, String shedshule, int roundsNumber, int exerciseNumber, String addDate, String firstDayTraining, String lastTrainingDayDate, int repetition, long averageTime) {
        this.traningName = treningName;
        this.weekDays = weekDays;
        this.trainingMode = trainingMode;
        this.shedshule = shedshule;
        this.roundsNumber = roundsNumber;
        this.exerciseNumber = exerciseNumber;
        this.addDate = addDate;
        this.firstDayTraining = firstDayTraining;
        this.lastTrainingDayDate = lastTrainingDayDate;
        this.repetition = repetition;
        this.averageTime = averageTime;
    }

    public TrainingValue(int trainingID, String weekDays, String trainingMode, String shedshule, int roundsNumber, int exerciseNumber, String addDate, String firstDayTraining, String lastTrainingDayDate, int repetition, long averageTime) {
        this.trainingId = trainingID;
        this.weekDays = weekDays;
        this.trainingMode = trainingMode;
        this.shedshule = shedshule;
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

    public String getTraningName() {
        return traningName;
    }

    public void setTraningName(String traningName) {
        this.traningName = traningName;
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

    public String getShedshule() {
        return shedshule;
    }

    public void setShedshule(String shedshule) {
        this.shedshule = shedshule;
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

