package com.example.daniel.values;

import android.appwidget.AppWidgetProviderInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.example.daniel.database.dataoldtrainings.OldTraining;
import com.example.daniel.database.exercise.values.ExerciseValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;

public class Training {

    public final static String NAME="name",TRAINING_NAME="training_name",NAME_ID="name_id", TRAINING_ID= "training_id", ROUND_NUMBER="round_number", REPS="reps", WEIGHT="weight", EXERCISE_NUMBER="exercise_number";
    private TrainingValue trainingValue;
    private ArrayList<Exercise> exercises;
    public Training(){
        exercises = new ArrayList<Exercise>();
    }
    public void align(int index,int indexOfRound){
        if(indexOfRound==exercises.get(index).rounds.size()-1) return;
        if(indexOfRound>exercises.get(index).rounds.size()) {
            for(int i=exercises.get(index).rounds.size();i<indexOfRound;i++)
                exercises.get(index).rounds.add(i, new ExerciseValue());
        } else {
            for(int i=exercises.get(index).rounds.size()-1;i>indexOfRound;i--){
                exercises.get(index).rounds.remove(i);
            }
        }
    }
    public void add(int index,ExerciseValue exerciseValue){
        exercises.add(index,new Exercise());
        exercises.get(index).rounds.add(0,exerciseValue);
    }
    public void add(int index, int roundIndex, ExerciseValue exerciseValue){
        if(exercises.size()-1!=index)exercises.add(index,new Exercise());
        if(roundIndex>exercises.get(index).rounds.size()) roundIndex=0;
        exercises.get(index).rounds.add(roundIndex,exerciseValue);
    }
    public ArrayList<Exercise> getExercises() {
        return exercises;
    }
    public void toTraining(OldTraining[][] oldTrainings){
        for(int i=0;i<oldTrainings.length;i++){
            for(int j=0;j<oldTrainings[i].length;j++){
                if(oldTrainings[i][j]==null)continue;
                add(i,j,new ExerciseValue(oldTrainings[i][j].getExerciseID(),oldTrainings[i][j].getTrainingID(),i+1,oldTrainings[i][j].getRoundNumber(),oldTrainings[i][j].getWeight(),oldTrainings[i][j].getReps()));
            }
        }
    }
    public int size(){
        return exercises.size();
    }
    public int absoluteSize(){
        for(int i=exercises.size();i>0;i--){
            if(!exercises.get(i-1).isNull()) return i;
        }
        return -1;
    }
    public Exercise get(int index){
        return exercises.get(index);
    }
    @SuppressWarnings("unchecked")
    public <T> T get(String tag,int exerciseNumber, int roundNumber){
        switch(tag) {
            case NAME:              return (T) exercises.get(exerciseNumber).rounds.get(roundNumber).getName();
            case TRAINING_NAME:     return (T) exercises.get(exerciseNumber).rounds.get(roundNumber).getTrainingName();
            case NAME_ID:           return (T) Integer.valueOf(exercises.get(exerciseNumber).rounds.get(roundNumber).getNameID());
            case TRAINING_ID:       return (T) Integer.valueOf(exercises.get(exerciseNumber).rounds.get(roundNumber).getTrainingID());
            case ROUND_NUMBER:      return (T) Integer.valueOf(exercises.get(exerciseNumber).rounds.get(roundNumber).getRoundNumber());
            case REPS:              return (T) Integer.valueOf(exercises.get(exerciseNumber).rounds.get(roundNumber).getReps());
            case EXERCISE_NUMBER:   return (T) Integer.valueOf(exercises.get(exerciseNumber).rounds.get(roundNumber).getExerciseNumber());
            case WEIGHT:            return (T) Double.valueOf(exercises.get(exerciseNumber).rounds.get(roundNumber).getWeight());
            default: break;

        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public <T> void set(String tag,T value, int exerciseNumber, int roundNumber){
            if(value.equals(null)||value.equals("null")) return;
        switch(tag) {
            case NAME:              exercises.get(exerciseNumber).rounds.get(roundNumber).setName((String) value);break;
            case TRAINING_NAME:     exercises.get(exerciseNumber).rounds.get(roundNumber).setTrainingName((String) value);break;
            case NAME_ID:           exercises.get(exerciseNumber).rounds.get(roundNumber).setNameID(Integer.valueOf(String.valueOf(value)));break;
            case TRAINING_ID:       exercises.get(exerciseNumber).rounds.get(roundNumber).setTrainingID(Integer.valueOf(String.valueOf(value)));break;
            case ROUND_NUMBER:      exercises.get(exerciseNumber).rounds.get(roundNumber).setRoundNumber(Integer.valueOf(String.valueOf(value)));break;
            case REPS:              exercises.get(exerciseNumber).rounds.get(roundNumber).setReps(Integer.valueOf(String.valueOf(value)));break;
            case EXERCISE_NUMBER:   exercises.get(exerciseNumber).rounds.get(roundNumber).setExerciseNumber(Integer.valueOf(String.valueOf(value)));break;
            case WEIGHT:            exercises.get(exerciseNumber).rounds.get(roundNumber).setWeight(Double.valueOf(String.valueOf(value)));break;
            default: break;

        }
    }
    public void setRound(ExerciseValue exerciseValue,int exerciseNumber, int roundNumber) {
        exercises.get(exerciseNumber).rounds.set(roundNumber, exerciseValue);
    }
    public ExerciseValue getRound(int exerciseNumber, int roundNumber){
        return exercises.get(exerciseNumber).rounds.get(roundNumber);
    }
    public void setExercises(List<ExerciseValue> exerciseList){

        for(ExerciseValue exerciseValue : exerciseList){
            exercises.add(new Exercise());
            exercises.get(exercises.size()-1).rounds.add(0,exerciseValue);
        }
    }

    public void move(int oldPos, int newPos) {
        Exercise exercise= exercises.get(oldPos);
        exercises.remove(oldPos);
        exercises.add(newPos,exercise);
    }

    public  class Exercise {
        private ArrayList<ExerciseValue> rounds;
        public Exercise(){
            rounds= new ArrayList<ExerciseValue>();
        }
        public int size(){
            return rounds.size();
        }
        public void setOneRound(){
            for(ExerciseValue r : rounds){
                if(rounds.indexOf(r)!=0) rounds.remove(r);
            }
        }
        public boolean isNull(){
            if(rounds.equals(null)||(rounds.size()==1&&rounds.get(0).getName()==null&&rounds.get(0).getNameID()==0&&rounds.get(0).getRoundNumber()==0&&
                    rounds.get(0).getExerciseNumber()==0&&rounds.get(0).getReps()==0&&rounds.get(0).getWeight()==0.0&&rounds.get(0).getTrainingName()==null&&rounds.get(0).getTrainingID()==0)) return true; else return false;
        }
    }
    /*class Round extends ExerciseValue{
        public Round(ExerciseValue exerciseValue){
            super(exerciseValue.getName(),exerciseValue.getNameID(),exerciseValue.getTrainingName(),exerciseValue.getTrainingID(),exerciseValue.getExerciseNumber(),exerciseValue.getRoundNumber(),exerciseValue.getWeight(),exerciseValue.getReps());
        }
    }*/
}
