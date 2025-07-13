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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

import pl.dsquare.gymassistant.api.ApiClient;
import pl.dsquare.gymassistant.api.ApiImport;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.stream.Collectors;

@Database(entities = {Exercise.class, Training.class}, version = VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME="gym_assistant_db";
    private Context c;
    public abstract  ExerciseDao exerciseDao();
    public abstract  TrainingDao trainingDao();
    private static ApiImport apiImport = new ApiClient().getRetrofitInstance().create(ApiImport.class);

    public static AppDatabase getDatabase(Context context) {
        return Room.databaseBuilder(context,AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }
    public static void init(Context context) {
        AppDatabase db = Room.databaseBuilder(context,AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
        db.exerciseDao().deleteAll();
        //db.exerciseDao().insertAll(Exercise.init(ExerciseNames.nameTrainings,POLISH));
        try {
            List<Exercise> exercises = apiImport.getExercises().execute().body();
            List<Exercise> presentExercises = db.exerciseDao().getAll();
            List<Exercise> result = new ArrayList<>();
            if(presentExercises.size()==0)
                db.exerciseDao().insertAll(exercises);
            else if(presentExercises!=null||presentExercises.size()==0){
                //TODO fill not present exercises
                /*for (Exercise exe : exercises  ) {
                    result = presentExercises.stream().filter(e-> e.getName().equals(exe.getName())).collect(Collectors.toList());
                    if(result.size()==0)
                        db.exerciseDao().insert(exe);
                 */

                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
