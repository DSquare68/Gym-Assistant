package pl.dsquare.gymassistant.db;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract interface TrainingDao {

    @Query("SELECT * FROM training")
    List<Training> getAll();

    @Query("SELECT * FROM training WHERE _ID IN (:exerciseIds)")
    List<Training> loadAllByIds(int[] exerciseIds);

    @Query("SELECT * FROM training WHERE  exercise_name_id = :first")
    Training findByExercise(int first);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Training... exercises);

    @Delete
    void delete(Training user);

    @Query("DELETE FROM training")
    void deleteAll();

    @Query("SELECT MAX(_ID) FROM training")
    int getMaxID();

    @Query("SELECT MAX(schema) FROM training")
    int getMAXSchemaID();

    @Query("SELECT exercise_name FROM training WHERE schema = 1")
    List<String> getAllSchemaNames();

    @Query("SELECT MIN(_ID) FROM training WHERE exercise_name = :string")
    int getIDSchema(String string);
}
