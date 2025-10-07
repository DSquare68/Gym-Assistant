package pl.dsquare.home.data;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dsquare.home.db.AppDatabase;

@Data
@NoArgsConstructor
@Entity(tableName = "TRAININGS")
public class TrainingRecord implements Cloneable{

    @PrimaryKey
    @Column(name = "ID")
    private int ID;
    @Column(name = "ID_TRAINING")
    private int ID_TRAINING;
    @Column(name = "ID_EXERCISE_NAME")
    private int ID_EXERCISE_NAME;
    @Column(name = "SERIE")
    private int SERIE;
    @Column(name = "REPEAT")
    private int REPEAT;
    @Column(name = "WEIGHT")
    private double WEIGHT;
    @Column(name = "SCHEMA")
    private int SCHEMA;
    @Column(name = "IS_SCHEMA")
    private int IS_SCHEMA;
    @Column(name = "DATE_TRAINING")
    private String DATE_TRAINING;
    @Column(name = "NAME_SCHEMA")
    private String NAME_SCHEMA;
    @Column(name = "TIME_TRAINING")
    private String TIME_TRAINING;

    public TrainingRecord(int ID,int ID_TRAINING, int ID_EXERCISE_NAME, int SERIE, int REPEAT, double WEIGHT, int SCHEMA, int IS_SCHEMA, String DATE_TRAINING, String NAME_SCHEMA, String TIME_TRAINING) {
        this.ID = ID;
        this.ID_TRAINING = ID_TRAINING;
        this.ID_EXERCISE_NAME = ID_EXERCISE_NAME;
        this.SERIE = SERIE;
        this.REPEAT = REPEAT;
        this.WEIGHT = WEIGHT;
        this.SCHEMA = SCHEMA;
        this.IS_SCHEMA = IS_SCHEMA;
        this.DATE_TRAINING = DATE_TRAINING;
        this.NAME_SCHEMA = NAME_SCHEMA;
        this.TIME_TRAINING = TIME_TRAINING;
    }
    @Ignore
    public TrainingRecord(int ID_TRAINING, int ID_EXERCISE_NAME, int SERIE, int REPEAT, double WEIGHT, int SCHEMA, int IS_SCHEMA, String DATE_TRAINING, String NAME_SCHEMA, String TIME_TRAINING) {
        this.ID_TRAINING = ID_TRAINING;
        this.ID_EXERCISE_NAME = ID_EXERCISE_NAME;
        this.SERIE = SERIE;
        this.REPEAT = REPEAT;
        this.WEIGHT = WEIGHT;
        this.SCHEMA = SCHEMA;
        this.IS_SCHEMA = IS_SCHEMA;
        this.DATE_TRAINING = DATE_TRAINING;
        this.NAME_SCHEMA = NAME_SCHEMA;
        this.TIME_TRAINING = TIME_TRAINING;
    }
    @Override
    public TrainingRecord clone() {
        try {
            return (TrainingRecord) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  // Can never happen
        }
    }
    public static Training toTraining(ArrayList<TrainingRecord> records, Context c){
        AppDatabase db = AppDatabase.getDatabase(c);
        Training tr = new Training(records.get(0).getNAME_SCHEMA());
        String exerciseName = "";
        int exerciseLP = -1;
        int serieLP = 0;
        tr.setID(records.get(0).getID_TRAINING());
        if(records.get(0).getIS_SCHEMA()==1)
            tr.setTemplete(true);
        else
            tr.setTemplete(false);
        for(TrainingRecord r : records){
            String name = db.exerciseDao().getNameByID(r.getID_EXERCISE_NAME());
            if(!exerciseName.equals(name)) {
                tr.getExercises().add(name);
                exerciseName=name;
                exerciseLP++;
            }
            if(r.getSERIE()==1) {
                tr.getRounds().put(tr.getExercises().get(exerciseLP), new ArrayList<>());
            }
            tr.getRounds().get(tr.getExercises().get(exerciseLP)).add(tr.new Round(r.getSERIE(),r.getREPEAT(),r.getWEIGHT()));

        }
        return tr;
    }
}


