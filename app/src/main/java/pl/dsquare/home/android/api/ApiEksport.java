package pl.dsquare.home.android.api;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import pl.dsquare.home.android.data.TrainingRecord;
import pl.dsquare.home.android.db.Exercise;
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
    @Headers("Content-Type: application/json")
    Call<ResponseBody> addTraining(@Body ArrayList<TrainingRecord> training);

    @POST("api/add/exercise_name")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> addExerciseName(@Body Exercise exercise);
}
