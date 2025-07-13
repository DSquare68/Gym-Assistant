package pl.dsquare.gymassistant.api;

import java.util.List;

import pl.dsquare.gymassistant.db.Exercise;
import pl.dsquare.gymassistant.db.ExerciseNames;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiImport {
    @GET("api/get/exercises")
    Call<List<Exercise>> getExercises();
}
