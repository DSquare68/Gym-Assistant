package pl.dsquare.home.android.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "exercises")
@Data
@NoArgsConstructor
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int language;

    public Exercise(int ID, String name) {
        this.name = name;
        this.id = ID;
    }
    @Ignore
    public Exercise(int ID, String name, int language) {
        this.id = ID;
        this.name = name;
        this.language =language;
    }
    public static Exercise[] init(String[] data, int language) {
        ArrayList<Exercise> exercises = new ArrayList<>();
        int i = 0;
        Arrays.stream(data).forEach(d -> exercises.add(new Exercise(exercises.size()+1,d,language)));
        return exercises.toArray(new Exercise[exercises.size()]);
    }
}
