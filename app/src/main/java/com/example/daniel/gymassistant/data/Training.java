package com.example.daniel.gymassistant.data;

import java.util.ArrayList;
import java.util.Map;

public class Training {

    private int _ID;
    ArrayList<String> exercises;
    Map<String,ArrayList<Round>> rounds;

    public Training(int _ID, ArrayList<String> exercises, Map<String, ArrayList<Round>> rounds) {
        this._ID = _ID;
        this.exercises = exercises;
        this.rounds = rounds;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public ArrayList<String> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<String> exercises) {
        this.exercises = exercises;
    }

    public Map<String, ArrayList<Round>> getRounds() {
        return rounds;
    }

    public void setRounds(Map<String, ArrayList<Round>> rounds) {
        this.rounds = rounds;
    }

    private class Round {
        int roundNumber,reps;
        double weight;

        public Round(int roundNumber, int reps, double weight) {
            this.roundNumber = roundNumber;
            this.reps = reps;
            this.weight = weight;
        }

        public int getRoundNumber() {
            return roundNumber;
        }

        public void setRoundNumber(int roundNumber) {
            this.roundNumber = roundNumber;
        }

        public int getReps() {
            return reps;
        }

        public void setReps(int reps) {
            this.reps = reps;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }
}
