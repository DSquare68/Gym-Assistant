package pl.dsquare.home.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import pl.dsquare.home.data.TrainingRecord;
@Dao
public abstract interface TrainingDao {

    @Query("SELECT * FROM trainings")
    List<TrainingRecord> getAll();

    @Query("SELECT * FROM trainings WHERE ID IN (:exerciseIds)")
    List<TrainingRecord> loadAllByIds(int[] exerciseIds);

    @Query("SELECT * FROM trainings WHERE  ID_EXERCISE_NAME = :first")
    List<TrainingRecord> trainingsfindByExercise(int first);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TrainingRecord> t);

    @Delete
    void delete(TrainingRecord user);

    @Query("DELETE FROM trainings")
    void deleteAll();

    @Query("SELECT MAX(ID) FROM trainings")
    int getMaxID();

    @Query("SELECT MAX(schema) FROM trainings")
    int getMAXSchemaID();

    @Query("SELECT DISTINCT(NAME_SCHEMA) FROM trainings WHERE IS_SCHEMA = 1")
    List<String> getAllSchemaNames();

    @Query("SELECT MIN(ID) FROM trainings WHERE ID_TRAINING = :ID")
    int getIDSchema(int ID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrainingRecord trainings);

    @Query("SELECT * FROM trainings WHERE is_schema = 1")
    List<TrainingRecord> gettrainings_recordSchemas();

    @Query("SELECT ID_TRAINING FROM trainings WHERE NAME_SCHEMA = :name AND IS_SCHEMA = 1")
    int getIDTrainingSchamaByName(String name);

    @Query("SELECT * FROM trainings WHERE ID_TRAINING = :id")
    List<TrainingRecord> getTrainigByIDTraining(int id);
}
