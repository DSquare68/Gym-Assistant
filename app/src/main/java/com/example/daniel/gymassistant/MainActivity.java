package com.example.daniel.gymassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    LinearLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (LinearLayout) View.inflate(this,R.layout.activity_main,null);
        setContentView(parent);
    }

    public void startTraining(View view) {
    }

    public void addTraining(View view) {
    }

    public void progressOpen(View view) {
    }

    public void timetableOpen(View view) {
    }

    public void synchOpen(View view) {
    }

    public void optionsOpen(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
