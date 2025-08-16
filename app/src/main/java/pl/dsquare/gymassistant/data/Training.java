package pl.dsquare.gymassistant.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class Training {

    private int ID;
    private String name;
    private ArrayList<String> exercises;
    private HashMap<String,ArrayList<Round>> rounds;
    private boolean templete;

    public Training(int _ID, ArrayList<String> exercises, HashMap<String, ArrayList<Round>> rounds) {
        this.ID = _ID;
        this.exercises = exercises;
        this.rounds = rounds;
    }
    public Training(String name){
        this.name = name;
        templete = false;
        exercises = new ArrayList<>();
        rounds = new HashMap<>();
    }

    public class Round {
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
