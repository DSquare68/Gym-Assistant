package com.example.daniel.gymassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.daniel.database.exercise.name.DatebaseOfexerciseNames;
import com.example.daniel.database.exercise.name.Exercise;
import com.example.daniel.database.exercise.name.ExerciseDatabase;
import com.example.daniel.values.Resolution;
import com.example.daniel.values.SettingsValues;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {
    LinearLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (LinearLayout) View.inflate(this,R.layout.activity_main,null);
        setContentView(parent);
        if(SettingsValues.getValue(SettingsValues.FIRST_OPEN_APP,getApplicationContext())==1){
            Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.welcome),Toast.LENGTH_LONG);
            toast.show();
            SettingsValues.setValue(SettingsValues.FIRST_OPEN_APP,getApplicationContext(),-1);
            for(int i = 0; i< DatebaseOfexerciseNames.nazwyTreningów.length; i++){
                ExerciseDatabase trainingName = new ExerciseDatabase(getApplicationContext());
                trainingName.addExercise(new Exercise(DatebaseOfexerciseNames.nazwyTreningów[i]));
            }
        }
        new Resolution(this);
    }

    public void startTraining(View view) {
        Intent intent = new Intent(this, StartTraining.class);
        startActivity(intent);

    }

    public void addTraining(View view) {
        Intent intent = new Intent(this, AddTraining.class);
        startActivity(intent);
    }

    public void progressOpen(View view) {
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
    }

    public void timetableOpen(View view) {
        Intent intent = new Intent(this, Timetable.class);
        startActivity(intent);
    }

    public void synchOpen(View view) {
        Intent intent = new Intent(this, Synch.class);
        startActivity(intent);
    }

    public void optionsOpen(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
