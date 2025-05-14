package pl.dsquare.gymassistant.data;

package com.dsquare.model;

import java.util.ArrayList;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "ADMIN", name = "TRAININGS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "training_seq")
    @SequenceGenerator(name = "training_seq", sequenceName = "SIMPLE", allocationSize = 1)
    //@SequenceGenerator(sequenceName = "SIMPLE", allocationSize = 1, name = "CUST_SEQ")
    private int id;
    private int idExerciseName;
    private int serie;
    private int repeat;
    private double weight;
    private int schema;
    private Date dateTraining;
    private String name;
    //private ArrayList<Exercise> exercises = new ArrayList<Exercise>();

    public TrainingModel(int idExerciseName, int serie, int repeat, double weight, int schema, Date dateTraining, String name) {
        this.idExerciseName = idExerciseName;
        this.serie = serie;
        this.repeat = repeat;
        this.weight = weight;
        this.schema = schema;
        this.dateTraining = dateTraining;
        this.name = name;
    }
    private int getWeight(int idExerciseName, int serie) {
        //TODO get weight from exerciseName
        return 0;
    }
    private int getWeight(int id) {
        //TODO get weight from exerciseName
        return 0;
    }
    private int getRepeat(int idExerciseName, int serie) {
        //TODO get weight from exerciseName
        return 0;
    }
    private int getRepeat(int id) {
        //TODO get weight from exerciseName
        return 0;
    }
}
