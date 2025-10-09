package pl.dsquare.home.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pl.dsquare.home.android.activity.CreateTrainingActivity;
import pl.dsquare.home.android.activity.SheduleActivity;
import pl.dsquare.home.android.activity.TrainActivity;
import pl.dsquare.home.android.android.MainActivity;
import pl.dsquare.home.android.android.R;

public class GymActivity extends Activity {
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gym);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sp = getSharedPreferences(getResources().getString(R.string.settings), MODE_PRIVATE);
    }

    public void create(View v){
        Intent i = new Intent(this, CreateTrainingActivity.class);
        startActivity(i);
    }

    public void train(View v){
        Intent i = new Intent(this, TrainActivity.class);
        startActivity(i);
    }

    public void shedule(View v){
        Intent i = new Intent(this, SheduleActivity.class);
        startActivity(i);
    }

    public void progress(View v){
        Intent i = new Intent(this, Process.class);
        startActivity(i);
    }

    public void home(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
