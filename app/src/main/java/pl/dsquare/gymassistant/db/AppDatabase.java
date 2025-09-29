package pl.dsquare.gymassistant.db;

import static pl.dsquare.gymassistant.Units.*;

import android.content.Context;
import android.widget.AutoCompleteTextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import okhttp3.ResponseBody;
import pl.dsquare.gymassistant.api.ApiClient;
import pl.dsquare.gymassistant.api.ApiEksport;
import pl.dsquare.gymassistant.api.ApiImport;
import pl.dsquare.gymassistant.data.MatchRecord;
import pl.dsquare.gymassistant.data.TrainingRecord;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.stream.Collectors;

@Database(entities = {Exercise.class, TrainingRecord.class,MatchRecord.class}, version = VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME="gym_assistant_db";
    private Context c;

    public static void insertExerciseName(Exercise exercise) {
        try {
            apiEksport.addExerciseName(exercise).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract  ExerciseDao exerciseDao();

    public abstract MatchDao matchDao();
    public static void insertTrainingRecord(Context applicationContext) {
        try{
            String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Response<ResponseBody> response = apiEksport.addTrainingRecord(new TrainingRecord(1,1,1,0,0.0,1,1,simpleDateFormat.format(new Date()),"asdf","")).execute();
           // ArrayList<TrainingRecord> t = new ArrayList<TrainingRecord>();
            //t.add(new TrainingRecord(1,1,1,0,0.0,1,1,simpleDateFormat.format(new Date()),"asdf"));
            //new TrainingRecord(2,2,2,2,0.0,1,1,simpleDateFormat.format(new Date()),"asdf");
            //Response<ResponseBody> response = apiEksport.addTraining(t).execute();
            response.code();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void insertTraining(ArrayList<TrainingRecord> trainings) {
        try {
            Response response = apiEksport.addTraining(trainings).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getExerciseNameId(String name) {
        Response r = null;
        try {
            r = apiImport.getExerciseIDByName(name).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (int) r.body();
    }

    public static List<Exercise> getAllExercises() {
        try {
            return apiImport.getExercises().execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract  TrainingDao trainingDao();
    private static ApiImport apiImport = new ApiClient().getRetrofitInstance().create(ApiImport.class);
    private static ApiEksport apiEksport = new ApiClient().getRetrofitInstance().create(ApiEksport.class);

    public static AppDatabase getDatabase(Context context) {
        return Room.databaseBuilder(context,AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }
    public static void init(Context context) {
        AppDatabase db = Room.databaseBuilder(context,AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
        //db.exerciseDao().deleteAll();
        //db.exerciseDao().insertAll(Exercise.init(ExerciseNames.nameTrainings,POLISH));
        synchExerciseNames(db);
        synchTrainingsSchema(db);
        synchSeasns(db);
    }

    private static void synchSeasns(AppDatabase db) {
        try {
            List<MatchRecord> matches = apiImport.getMatches().execute().body();
            List<MatchRecord> presentMaches = db.matchDao().getAll();
            if(presentMaches.isEmpty() || presentMaches == null)
                db.matchDao().insertAll(matches);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private static void synchTrainingsSchema(AppDatabase db) {
        try {
            List<TrainingRecord> trainings = apiImport.getTrainingSchemas().execute().body();
            List<TrainingRecord> presentTrainings = db.trainingDao().getAll();
            if(trainings.size()!=presentTrainings.size()) {
                db.trainingDao().deleteAll();
                db.trainingDao().insertAll(trainings);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void synchExerciseNames(AppDatabase db) {
        try {
            List<Exercise> exercises = apiImport.getExercises().execute().body();
            List<Exercise> presentExercises = db.exerciseDao().getAll();
            if(exercises.size()!= presentExercises.size()){
                db.exerciseDao().deleteAll();
                db.exerciseDao().insertAll(exercises);
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
