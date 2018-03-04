package com.example.daniel.gymassistant;

import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.database.exercise.name.Exercise;
import com.example.daniel.extraview.Diagram;
import com.example.daniel.extraview.Scale;
import com.example.daniel.procedures.Units;

public class StatisticsCompareExercise extends AppCompatActivity {
    LinearLayout parentLayout;
    Diagram diagram;
    public  static Exercise exercise;
    //Todo: pobawić się skalą
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentLayout = (LinearLayout) View.inflate(this,R.layout.activity_statistics_compare_exercise,null);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        diagram = new Diagram(this, exercise);
        if(!diagram.isExercise) {
            Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_trainings),Toast.LENGTH_LONG);
            toast.show();
            this.finish();

        }
        diagram.setLayoutParams(new LinearLayout.LayoutParams(width- Units.dpToPx(this,30)*2, ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout tabelaISkala = new LinearLayout(this);
        tabelaISkala.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int skala=1;
        if(Diagram.maxWeight>10&&Diagram.maxWeight<20){
            skala=2;
        } else if(Diagram.maxWeight<50&&Diagram.maxWeight>20){
            skala=5;
        } else if(Diagram.maxWeight>=50){
            skala=10;
        }
        Scale weightScale= new Scale(this,1,skala,Diagram.maxWeight);
        skala=1;
        if(Diagram.maxReps>10&&Diagram.maxReps<20){
            skala=2;
        } else if(Diagram.maxReps<50&&Diagram.maxReps>20){
            skala=5;
        } else if(Diagram.maxReps>=50){
            skala=10;
        }
        Scale repsScale = new Scale(this,1,skala,Diagram.maxReps);
        tabelaISkala.addView(weightScale,0);
        tabelaISkala.addView(diagram,1);
        tabelaISkala.addView(repsScale,2);

        LinearLayout marginTop = setMarginTop();

        LinearLayout nazwyISerie = new LinearLayout(this);

        parentLayout.addView(marginTop);
        parentLayout.addView(tabelaISkala);

        setContentView(parentLayout);
    }

    private LinearLayout setMarginTop() {
        LinearLayout marginTop = new LinearLayout(this);
        marginTop.setOrientation(LinearLayout.HORIZONTAL);
        marginTop.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Units.dpToPx(this,20)));
        TextView weight = new TextView(this);
        weight.setText(getResources().getString(R.string.weight));
        weight.setTextColor(Color.parseColor("#0909c6"));
        weight.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1));
        weight.setGravity(Gravity.LEFT);
        TextView reps= new TextView(this);
        reps.setText(getResources().getString(R.string.reps));
        reps.setTextColor(Color.parseColor("#00d000"));
        reps.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1));
        reps.setGravity(Gravity.RIGHT);

        marginTop.addView(weight);
        marginTop.addView(reps);
        return  marginTop;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
