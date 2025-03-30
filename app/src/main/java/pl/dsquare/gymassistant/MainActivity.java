package pl.dsquare.gymassistant;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.room.Room;

import pl.dsquare.gymassistant.activity.CreateTrainingActivity;
import pl.dsquare.gymassistant.activity.SheduleActivity;
import pl.dsquare.gymassistant.activity.TrainActivity;
import pl.dsquare.gymassistant.db.AppDatabase;
import pl.dsquare.gymassistant.db.DB;
import pl.dsquare.gymassistant.db.Exercise;
import pl.dsquare.gymassistant.db.Sync;

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

    }

    private void firstRun() {
        if(sp.getBoolean(getResources().getString(R.string.is_first_run),true)) {
            new Thread(()->AppDatabase.init(getApplicationContext())).start();
            sp.edit().putBoolean(getResources().getString(R.string.is_first_run),false).apply();
        }
    }

    public void create(View v){
        Intent i = new Intent(this,CreateTrainingActivity.class);
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
    public void sync(View v){
        new Thread(()->new Sync().sync(this)).start();
    }
}
