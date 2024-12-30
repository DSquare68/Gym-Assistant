package pl.dsquare.gymassistant.db;

import android.content.Context;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import pl.dsquare.gymassistant.api.ApiEksport;
import pl.dsquare.gymassistant.api.ApiClient;
import pl.dsquare.gymassistant.api.ApiImport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OracleSchema {
    private List<Exercise> exerciseList;
    private Context c;
    private ApiEksport apiExport = ApiClient.getRetrofitInstance().create(ApiEksport.class);
    private ApiImport apiImport = ApiClient.getRetrofitInstance().create(ApiImport.class);
    public OracleSchema(Context c, List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
        this.c=c;
    }

    public void sync() {
        List<Exercise> exerList = apiImport.getExercises();
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
