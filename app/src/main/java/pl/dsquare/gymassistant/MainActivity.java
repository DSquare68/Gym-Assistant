package pl.dsquare.gymassistant;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import pl.dsquare.gymassistant.activity.CreateTrainingActivity;
import pl.dsquare.gymassistant.activity.SheduleActivity;
import pl.dsquare.gymassistant.activity.TrainActivity;
import pl.dsquare.gymassistant.db.AppDatabase;
import pl.dsquare.gymassistant.db.Exercise;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ConstraintLayout parent;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (ConstraintLayout) View.inflate(this, R.layout.activity_main, null);
        setContentView(parent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu_white);
        firstRun();
    }

    private void firstRun() {
        if(SettingsValues.getValue(SettingsValues.FIRST_OPEN_APP,getApplicationContext())==1){
            Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.welcome),Toast.LENGTH_LONG);
            toast.show();
            SettingsValues.setValue(SettingsValues.FIRST_OPEN_APP,getApplicationContext(),-1);
            final AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, AppDatabase.DB_NAME).build();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < pl.dsquare.gymassistant.ExerciseNames.exerciseNames.length; i++) {
                        db.exerciseDao().insert(new Exercise(pl.dsquare.gymassistant.ExerciseNames.exerciseNames[i]));
                    }
                }
            });
            t.start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
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
}
