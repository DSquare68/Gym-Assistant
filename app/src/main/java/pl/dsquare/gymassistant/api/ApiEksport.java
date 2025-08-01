package pl.dsquare.gymassistant.api;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pl.dsquare.gymassistant.data.TrainingModel;
import pl.dsquare.gymassistant.data.TrainingRecord;
import pl.dsquare.gymassistant.db.Exercise;
import pl.dsquare.gymassistant.db.ExerciseNames;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiEksport {

    @POST("api/add/exercise")
    Call<Exercise> postExercise(@Body Exercise e);
    //@POST("/api/add/exercises")
    //Call<Exercise> postExercises(@Body List<Exercise> e);
    @Headers("Content-Type: application/json")
    @POST("api/add/training_record")
    Call<ResponseBody> addTrainingRecord(@Body TrainingRecord training_record);
    @POST("api/add/training")
    Call<ArrayList<ResponseBody>> addTraining(@Body ArrayList<TrainingRecord> training);
}
