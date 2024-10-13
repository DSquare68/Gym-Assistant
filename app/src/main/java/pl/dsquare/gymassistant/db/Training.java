package pl.dsquare.gymassistant.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity(tableName = "training")
@Data
@AllArgsConstructor
public class Training {

    @PrimaryKey(autoGenerate = true)
    public int _ID;

    @ColumnInfo(name = "training_info_id")
    public int trainingID;

    @ColumnInfo(name = "exercise_name_id")
    public int exerciseNameID;

    @ColumnInfo(name = "training_name")
    public String trainingName;

    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "weight")
    public double weight;

    @ColumnInfo(name = "reps")
    public int reps;

    @ColumnInfo(name = "serie")
    public int serie;

    @ColumnInfo(name ="template_family")
    public int templateFamily;

    @ColumnInfo(name = "schema")
    public boolean schema;

    public Training(int trainingID, int exercise, double weight, int reps, int templateFamily)
    {
        this.trainingID = trainingID;
        this.exerciseNameID = exercise;
        this.weight = weight;
        this.reps = reps;
        this.templateFamily = templateFamily;
        this.schema=true;
    }
    public Training(int trainingID, int exercise, double weight, int reps,int serie, int templateFamily)
    {
        this.trainingID = trainingID;
        this.exerciseNameID = exercise;
        this.weight = weight;
        this.reps = reps;
        this.serie = serie;
        this.templateFamily = templateFamily;
        this.schema=true;
    }
}

