package pl.dsquare.home.android.android;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;

import pl.dsquare.home.android.FootballActivity;
import pl.dsquare.home.android.GymActivity;
import pl.dsquare.home.android.data.ExerciseNames;
import pl.dsquare.home.android.db.AppDatabase;
import pl.dsquare.home.android.db.Sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity  {

    private LinearLayout parent;
    private AppBarConfiguration appBarConfiguration;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (LinearLayout) View.inflate(this, R.layout.activity_main, null);
        sp = getSharedPreferences(getResources().getString(R.string.settings), MODE_PRIVATE);
        setContentView(parent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu_white);
        firstRun();
        // Create your list of ExerciseNames
        List<ExerciseNames> exerciseList = new ArrayList<>();
    }
    private void firstRun() {
       // if(sp.getBoolean(getResources().getString(R.string.is_first_run),true)) {
        new Thread(()-> AppDatabase.init(getApplicationContext())).start();
        //new Thread(()-> AppDatabase.getExerciseNameId("Wyciskanie sztangielek w leżeniu na ławce skośnej-głową w dół")).start();
        //new Thread(()-> AppDatabase.insertTrainingRecord(getApplicationContext())).start();
           // sp.edit().putBoolean(getResources().getString(R.string.is_first_run),false).apply();
        //}
    }

    public void sync(View v){
        new Thread(()->new Sync().sync(this)).start();
    }

    public void gym(View view) {
        Intent i = new Intent(this, GymActivity.class);
        startActivity(i);
    }
    public void football(View view) {
        Intent i = new Intent(this, FootballActivity.class);
        startActivity(i);
    }
}
