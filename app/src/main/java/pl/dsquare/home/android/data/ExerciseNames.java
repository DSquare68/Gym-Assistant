package pl.dsquare.home.android.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "ADMIN", name = "EXERCISE_NAMES")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseNames {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//, generator = "SEQUENCE1")
    //@SequenceGenerator(sequenceName = "SEQUENCE1", allocationSize = 1, name = "CUST_SEQ")
    //@Column(name = "ID")
    private int id;
    @Column(name = "NAME")
    private String name;
    public ExerciseNames(String name) {
        this.name = name;
    }
    /*
    public static ArrayList<ExerciseNames> init(String[] data) {
        ArrayList<ExerciseNames> exercises = new ArrayList<>();
        for(int i = 0; i < data.length; i++)
            exercises.add(new ExerciseNames(data[i]));
        return new ArrayList<ExerciseNames>(exercises.stream().filter(e->e!=null).toList());
    }
     */
}
