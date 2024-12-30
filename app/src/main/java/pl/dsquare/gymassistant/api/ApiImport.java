package pl.dsquare.gymassistant.api;

import java.util.List;

import pl.dsquare.gymassistant.db.Exercise;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiImport {
    @POST("/api/get/exercise")
    List<Exercise> getExercise();
    @POST("/api/get/exercises")
    List<Exercise> getExercises();
}
