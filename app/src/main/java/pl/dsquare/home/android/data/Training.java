package pl.dsquare.home.android.data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;
import pl.dsquare.home.android.db.AppDatabase;

@Data
public class Training {

    private int ID;
    private String name;
    private ArrayList<String> exercises;
    private LocalTime timeOfTraining;
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

    public static ArrayList<TrainingRecord> toTrainingRecord(AppDatabase db, Training training, String name, String date, int ID_Schema,String time) {
        ArrayList<TrainingRecord> records = new ArrayList<>();
        for(String nameExercise : training.getExercises()){
            int idExercise = db.exerciseDao().getIDByName(nameExercise);
            for(Round round : training.getRounds().get(nameExercise)) {
                TrainingRecord record = new TrainingRecord(
                        0,
                        idExercise,
                        round.getRoundNumber(),
                        round.getReps(),
                        round.getWeight(),
                        ID_Schema,
                        0, // Assuming 0 for trainingID
                        date,
                        name,
                        time
                );
                records.add(record);
            }
        }
        return records;
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
