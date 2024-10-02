package pl.dsquare.gymassistant.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {Exercise.class, Training.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME="gym_assistant_db";
    private Context c;
    public abstract  ExerciseDao exerciseDao();
    public abstract  TrainingDao trainingDao();

    public static AppDatabase getDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    @Override
    public void clearAllTables() {
        exerciseDao().deleteAll();
        trainingDao().deleteAll();
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(@NonNull DatabaseConfiguration databaseConfiguration) {
        return (SupportSQLiteOpenHelper)new OpenHelper(databaseConfiguration.context,databaseConfiguration.name);
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

}
