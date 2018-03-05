package com.example.daniel.gymassistant;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.daniel.database.dataoldtrainings.OldTraining;
import com.example.daniel.database.dataoldtrainings.OldTrainingsDatabase;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValuesDatabase;

public class StatisticsCompareTraining extends AppCompatActivity {
    /**
     * first dimension: 0-previous, 1-comparing
     * second dimension rounds
     * third dimension exercise
     */
    public static OldTraining[][][] oldTrainings;
    TrainingValue trainingValue;
    static LinearLayout[] exercise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_compare_training);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(oldTrainings[1][0][0].getTrainingName());
        TrainingValuesDatabase wtd = new TrainingValuesDatabase(getApplicationContext());
        trainingValue = wtd.getByTrainingID(oldTrainings[1][0][0].getTrainingID());
        setScrollExercise();
    }

    private void setScrollExercise() {
        ScrollView scrollView = this.findViewById(R.id.scroll_view_statistics_compare_training);
        exercise = new LinearLayout[trainingValue.getExerciseNumber()];
        OldTrainingsDatabase dstd = new OldTrainingsDatabase(getApplicationContext(), oldTrainings[1][0][0].getTrainingName());
        int roundNumber[] =  dstd.getRoundsNumber(oldTrainings[1][0][0].getTrainingDate(), oldTrainings[1][0][0].getTrainingTime());
        HorizontalScrollView[] scrollRounds = new HorizontalScrollView[roundNumber.length];
        for(int i = 0; i< trainingValue.getExerciseNumber(); i++){
            if(oldTrainings[1].length<=i) return;
            TextView name  = new TextView(this);
            exercise[i] = new LinearLayout(this);
            int marginSide = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
            int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
            exercise[i].setPadding(marginSide,marginTop,marginSide,0);
            exercise[i].setOrientation(LinearLayout.VERTICAL);
            scrollRounds[i] = new HorizontalScrollView(this);
            LinearLayout LL = new LinearLayout(this);
            LL.setOrientation(LinearLayout.HORIZONTAL);
            scrollRounds[i].addView(LL,0, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            name.setText(oldTrainings[1][i][0].getExerciseName());
            for(int j=0;j<roundNumber[i];j++){
                LinearLayout roundLL = (LinearLayout) View.inflate(this,R.layout.start_training_exercise_round,null);
                roundLL.setOrientation(LinearLayout.HORIZONTAL);
                TextView weight = roundLL.findViewById(R.id.weight);
                if(!(oldTrainings[1].length<i|| oldTrainings[1][i].length<=j)&& oldTrainings[1][i][j]!=null)weight.setText(String.valueOf(oldTrainings[1][i][j].getWeight()));
                if(oldTrainings[0]==null || oldTrainings[0].length<i|| oldTrainings[0][i].length<=j) {

                } else if(oldTrainings[0][i][j]==null){
                    weight.setBackgroundColor(Color.WHITE);
                } else if(oldTrainings[1][i][j]==null){
                    weight.setBackgroundColor(Color.BLACK);
                } else if(oldTrainings[1][i][j].getWeight()== oldTrainings[0][i][j].getWeight()) {
                    weight.setBackgroundColor(Color.BLUE);
                } else if(oldTrainings[1][i][j].getWeight()> oldTrainings[0][i][j].getWeight()){
                    weight.setBackgroundColor(Color.GREEN);
                } else {
                    weight.setBackgroundColor(Color.RED);
                }

                TextView reps = roundLL.findViewById(R.id.reps);
                if(oldTrainings[0]==null || oldTrainings[0].length<i|| oldTrainings[0][i].length<=j) {

                } else if(oldTrainings[0][i][j]==null){
                    reps.setBackgroundColor(Color.WHITE);
                } else if(oldTrainings[1][i][j]==null){
                    reps.setBackgroundColor(Color.BLACK);
                } else if(oldTrainings[1][i][j].getReps()== oldTrainings[0][i][j].getReps()){
                    reps.setBackgroundColor(Color.BLUE);
                } else if(oldTrainings[1][i][j].getReps()> oldTrainings[0][i][j].getReps()){
                    reps.setBackgroundColor(Color.GREEN);
                } else {
                    reps.setBackgroundColor(Color.RED);
                }
                if(!(oldTrainings[1].length<i|| oldTrainings[1][i].length<=j)&& oldTrainings[1][i][j]!=null)reps.setText(String.valueOf(oldTrainings[1][i][j].getReps()));
                ((LinearLayout) scrollRounds[i].getChildAt(0)).addView(roundLL,j);
            }
            //((LinearLayout) name.getParent()).removeView(name);
            exercise[i].addView(name,0);
            exercise[i].addView(scrollRounds[i],1);
            ((LinearLayout) scrollView.getChildAt(0)).addView(exercise[i],i);
        }

    }

}
