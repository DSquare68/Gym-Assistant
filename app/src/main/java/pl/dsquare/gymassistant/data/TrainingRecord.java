package pl.dsquare.gymassistant.data;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(schema = "ADMIN", name = "TRAININGS")
@Data
public class TrainingRecord {

    @Id
    private int ID;
    private int ID_TRAINING;
    private int ID_EXERCISE_NAME;
    private int SERIE;
    private int REPEAT;
    private double WEIGHT;
    private int SCHEMA;
    private int IS_SCHEMA;
    private Date DATE_TRAINING;
    private String NAME_SCHEMA;


}


