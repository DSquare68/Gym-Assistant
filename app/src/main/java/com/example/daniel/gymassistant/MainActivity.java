package com.example.daniel.gymassistant;



import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;

import java.sql.Time;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout parent;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (DrawerLayout) View.inflate(this,R.layout.activity_main,null);
        setContentView(parent);

        navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu_white);


        init();
    }

    private void init(){

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, parent);

        navigationView.setNavigationItemSelectedListener(this);

        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(parent)
                        .build();

        NavigationUI.setupWithNavController(navigationView, navController);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){



            case R.id.nav_posts:{



                break;
            }
        }

        item.setChecked(true);
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
