package pl.dsquare.gymassistant.api;

import java.util.List;

import pl.dsquare.gymassistant.db.Exercise;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiImport {
    final String IP = "10.98.0.29";
    @POST("http:/"+IP+":8080/api/get/exercise")
    Call<List<Exercise>> getExercise();
    @POST("http:/"+IP+":8080/api/get/exercises")
    Call<List<Exercise>> getExercises();
}
