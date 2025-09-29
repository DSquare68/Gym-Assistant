package pl.dsquare.gymassistant.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import pl.dsquare.gymassistant.data.MatchRecord;
import pl.dsquare.gymassistant.data.TrainingRecord;
import pl.dsquare.gymassistant.db.Exercise;
import pl.dsquare.gymassistant.db.ExerciseNames;
import pl.dsquare.gymassistant.db.Training;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiImport {
    @GET("api/get/exercises")
    Call<List<Exercise>> getExercises();

    @GET("api/get/exerciseIDByName/{name}")
    Call<Integer> getExerciseIDByName(@Path("name") String name);

    @GET("api/get/TrainingSchemas")
    Call<List<TrainingRecord>> getTrainingSchemas();

    @GET("api/get/matches")
    Call<List<MatchRecord>> getMatches();
}
