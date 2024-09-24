package pl.dsquare.gymassistant;


import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import pl.dsquare.gymassistant.db.AppDatabase;
import pl.dsquare.gymassistant.db.Exercise;
import com.google.android.material.navigation.NavigationView;
import pl.dsquare.gymassistant.ExerciseNames;
import java.util.Objects;
import pl.dsquare.gymassistant.R.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout parent;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DrawerLayout) View.inflate(this, R.layout.activity_main, null);
        setContentView(parent);

        navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu_white);

        init();
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
                    for (int i = 0; i < ExerciseNames.exerciseNames.length; i++) {
                        db.exerciseDao().insert(new Exercise(ExerciseNames.exerciseNames[i]));
                    }
                }
            });
            t.start();
        }
    }

    private void init() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, parent);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(parent)
                        .build();

        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

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
        if(item.getItemId()==R.id.menu_home)
            return true;
        else if(item.getItemId()== id.menu_add_training)
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_home_to_add_training);
        else if(item.getItemId()== id.menu_training)
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_home_to_training);
        else if(item.getItemId()== id.menu_show_training)
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_home_to_show_training);
        parent.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
