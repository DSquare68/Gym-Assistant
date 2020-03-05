package com.example.daniel.gymassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {
    LinearLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (LinearLayout) View.inflate(this,R.layout.activity_main,null);
        setContentView(parent);

    }
}
