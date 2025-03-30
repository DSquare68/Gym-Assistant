package pl.dsquare.gymassistant.db;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import pl.dsquare.gymassistant.api.ApiEksport;
import pl.dsquare.gymassistant.api.ApiClient;
import pl.dsquare.gymassistant.api.ApiImport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OracleSchema {
    private List<Exercise> exerciseList;
    private List<Exercise> exerList = null;
    private Context c;
    private ApiEksport apiExport = ApiClient.getRetrofitInstance().create(ApiEksport.class);
    private ApiImport apiImport = ApiClient.getRetrofitInstance().create(ApiImport.class);
    public OracleSchema(Context c, List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        this.c=c;
    }

    public void sync() {
        Call<List<Exercise>> exerCall = apiImport.getExercises();
        final CountDownLatch latch = new CountDownLatch(1);
        exerCall.timeout().timeout(3, TimeUnit.SECONDS);
        exerCall.enqueue(new Callback<List<Exercise>>() {

            @Override
            public void onResponse(Call<List<Exercise>> call, Response<List<Exercise>> response) {
                if (response.isSuccessful()) {
                    exerList = response.body();
                    Toast.makeText(c, "Exercises received successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(c, "Failed to receive exercises", Toast.LENGTH_SHORT).show();
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Exercise>> call, Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(c, "Error receiving exercises: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                latch.countDown();
            }
        });
        try{
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Exercise> missingExercises = exerciseList.stream()
                .filter(exercise -> !exerList.contains(exercise))
                .collect(Collectors.toList());
        Call<Exercise> call = apiExport.postExercises(missingExercises);
        call.enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                Toast.makeText(c,"SUCCESS",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable throwable) {
                Toast.makeText(c,"FAIL",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<Exercise> rsToLists(ResultSet resultSet) {
        try {
            List<Exercise> exerciseList = new java.util.ArrayList<>();
            while (resultSet.next()) {
                exerciseList.add(new Exercise(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3)));
            }
            return exerciseList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
