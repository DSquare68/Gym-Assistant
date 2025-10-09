package pl.dsquare.home.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //private static final String BASE_URL = "http://130.61.173.10:8080/home/";
    private static final String BASE_URL = "http://192.168.1.64:8080/";
    private static Retrofit retrofit;

    static Gson gson = new GsonBuilder()
            .setLenient()  // Enable lenient mode
            .create();
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}

