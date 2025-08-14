package pl.dsquare.gymassistant.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ExerciseDao {
    @Query("SELECT name FROM exercises")
    List<String> getAllNames();
    @Query("SELECT * FROM exercises")
    List<Exercise> getAll();

    @Query("SELECT * FROM exercises WHERE id IN (:exerciseIds)")
    List<Exercise> loadAllByIds(int[] exerciseIds);

    @Query("SELECT * FROM exercises WHERE name LIKE :first LIMIT 1")
    Exercise findByName(String first);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Exercise> exercises);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Exercise exercise);

    @Delete
    void delete(Exercise user);

    @Query("DELETE FROM exercises")
    void deleteAll();

    @Query("SELECT id FROM exercises WHERE name = :exercise")
    int getIDByName(String exercise);

    @Query("SELECT MAX(id) FROM exercises")
    int getMaxID();
}
