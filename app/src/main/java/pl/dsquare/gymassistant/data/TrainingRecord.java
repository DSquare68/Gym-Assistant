package pl.dsquare.gymassistant.data;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

public class TrainingRecord {

    private int ID;
    private int ID_TRAINING;
    private int ID_EXERCISE_NAME;
    private int SERIE;
    private int REPEAT;
    private double WEIGHT;
    private int SCHEMA;
    private int IS_SCHEMA;
    private String DATE_TRAINING;
    private String NAME_SCHEMA;

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
}


