package pl.dsquare.gymassistant.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity(tableName = "exercises")
@Data
@AllArgsConstructor
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int _ID;
    private String name;
    private int language;

    public static Exercise[] init(String[] data, int language) {
        ArrayList<Exercise> exercises = new ArrayList<>();
        int i = 0;
        Arrays.stream(data).forEach(d -> exercises.add(new Exercise(exercises.size()+1,d,language)));
        return exercises.toArray(new Exercise[exercises.size()]);
    }
}
