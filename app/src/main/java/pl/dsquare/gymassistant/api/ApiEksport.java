package pl.dsquare.gymassistant.api;

import java.util.List;

import pl.dsquare.gymassistant.data.TrainingModel;
import pl.dsquare.gymassistant.db.Exercise;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiEksport {

    @POST("/api/add/exercise")
    Call<Exercise> postExercise(@Body Exercise e);
    @POST("/api/add/exercises")
    Call<Exercise> postExercises(@Body List<Exercise> e);

    @POST("/api/add/trainings")
    Call<Exercise> postTrainings(@Body List<TrainingModel> e);
}
