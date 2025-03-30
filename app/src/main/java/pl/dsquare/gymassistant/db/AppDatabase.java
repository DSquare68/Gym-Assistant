package pl.dsquare.gymassistant.db;

import static pl.dsquare.gymassistant.Units.*;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.Insert;
import androidx.room.InvalidationTracker;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Callable;

@Database(entities = {Exercise.class, Training.class}, version = VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME="gym_assistant_db";
    private Context c;
    public abstract  ExerciseDao exerciseDao();
    public abstract  TrainingDao trainingDao();

    public static AppDatabase getDatabase(Context context) {
        return Room.databaseBuilder(context,AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }
    public static void init(Context context) {
        AppDatabase db = Room.databaseBuilder(context,AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
        db.exerciseDao().insertAll(Exercise.init(ExerciseNames.nameTrainings,POLISH));

    }
    private static Callable<InputStream> initExerciseNames(){
         return () -> {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Arrays.stream(ExerciseNames.nameTrainings).forEach(name -> {
                try {
                    bos.write(name.getBytes());
                    bos.write(System.lineSeparator().getBytes()); // optional: add newline separator
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return new ByteArrayInputStream(bos.toByteArray());
        };

    }
    @Override
    public void clearAllTables() {
        exerciseDao().deleteAll();
        trainingDao().deleteAll();
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(@NonNull DatabaseConfiguration databaseConfiguration) {
        return (SupportSQLiteOpenHelper)new OpenHelper(VERSION);
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
