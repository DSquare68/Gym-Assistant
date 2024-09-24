package pl.dsquare.gymassistant.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exercise_name")
    List<Exercise> getAll();

    @Query("SELECT * FROM exercise_name WHERE _ID IN (:exerciseIds)")
    List<Exercise> loadAllByIds(int[] exerciseIds);

    @Query("SELECT * FROM exercise_name WHERE name LIKE :first LIMIT 1")
    Exercise findByName(String first);

    @Insert
    void insertAll(Exercise... exercises);

    @Insert
    void insert(Exercise exercise);

    @Delete
    void delete(Exercise user);
}

