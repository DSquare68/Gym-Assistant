package pl.dsquare.gymassistant.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public TrainingRecord(int ID,int ID_TRAINING, int ID_EXERCISE_NAME, int SERIE, int REPEAT, double WEIGHT, int SCHEMA, int IS_SCHEMA, String DATE_TRAINING, String NAME_SCHEMA) {
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
    }
    @Ignore
    public TrainingRecord(int ID_TRAINING, int ID_EXERCISE_NAME, int SERIE, int REPEAT, double WEIGHT, int SCHEMA, int IS_SCHEMA, String DATE_TRAINING, String NAME_SCHEMA) {
        this.ID_TRAINING = ID_TRAINING;
        this.ID_EXERCISE_NAME = ID_EXERCISE_NAME;
        this.SERIE = SERIE;
        this.REPEAT = REPEAT;
        this.WEIGHT = WEIGHT;
        this.SCHEMA = SCHEMA;
        this.IS_SCHEMA = IS_SCHEMA;
        this.DATE_TRAINING = DATE_TRAINING;
        this.NAME_SCHEMA = NAME_SCHEMA;
    }
    @Override
    public TrainingRecord clone() {
        try {
            return (TrainingRecord) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  // Can never happen
        }
    }
}


