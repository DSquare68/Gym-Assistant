package pl.dsquare.home.api;

import java.util.List;

import pl.dsquare.home.data.MatchRecord;
import pl.dsquare.home.data.TrainingRecord;
import pl.dsquare.home.db.Exercise;
import retrofit2.Call;
import retrofit2.http.GET;
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
