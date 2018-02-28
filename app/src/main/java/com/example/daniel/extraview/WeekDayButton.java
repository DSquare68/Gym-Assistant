package com.example.daniel.extraview;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Date;


import com.example.daniel.database.exercise.values.ExerciseValuesDatabase;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValuesDatabase;

import com.example.daniel.gymassistant.AddTraining;
import com.example.daniel.gymassistant.R;
import com.example.daniel.gymassistant.Timetable;
import com.example.daniel.procedures.DateTraining;
import com.example.daniel.procedures.Units;
import com.example.daniel.values.AddTrainingValues;


/**
 * Created by Daniel on 2017-07-27.
 */

public class WeekDayButton extends android.support.v7.widget.AppCompatButton {
    int number =0;
    boolean isTraining =false, currentMonth;
    DateTraining dt= new DateTraining(getContext());
    Date trainingDate =null;
    TrainingValue trainingValue;
    // Todo: what if more then one training in day
    static TrainingValue chosenTrainingValue;
    public WeekDayButton(final Context context, int number, TrainingValue trainingValue, boolean currentMonth, Date trainingDate) {
        super(context,null);
        this.number =number;
        this.trainingValue =trainingValue;
        this.currentMonth =currentMonth;
        this.trainingDate =trainingDate;
        sprCzyJestTrening();
        init();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenTrainingValue = WeekDayButton.this.trainingValue;
                Timetable.parentLayout.removeViewAt(2);
                ScrollView scrollView = new ScrollView(v.getContext());
                LinearLayout scrollLL = new LinearLayout(v.getContext());
                scrollView.addView(scrollLL);
                LinearLayout trening =(LinearLayout) View.inflate(v.getContext(), R.layout.timetable_training,null);
                trening.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExerciseValuesDatabase wtd = new ExerciseValuesDatabase(context);
                        TrainingValuesDatabase wtrend = new TrainingValuesDatabase(context);
                        AddTraining.trainingValue = wtrend.get(WeekDayButton.this.trainingValue.getTrainingName());
                        AddTraining.exerciseValues = wtd.get(WeekDayButton.this.trainingValue.getTrainingName());
                        AddTraining.defaultTrainingName = WeekDayButton.this.trainingValue.getTrainingName();
                        AddTraining.openMode = AddTrainingValues.OPEN_FROM_SCHEDULE;
                        Intent intent = new Intent(context, AddTraining.class);
                        context.startActivity(intent);
                    }
                });
                TextView name =trening.findViewById(R.id.training_name);
                TextView dataTV = trening.findViewById(R.id.date_of_training);
                TextView exerciseNumber= trening.findViewById(R.id.number_of_exercises);
                TextView timeExercise = trening.findViewById(R.id.time);
                ImageView deleteTraining = trening.findViewById(R.id.delete_icon);
                deleteTraining.setVisibility(View.GONE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                trening.setLayoutParams(lp);
                lp.weight=-1;
                deleteTraining.setLayoutParams(lp);
                if(chosenTrainingValue !=null) {
                    name.setText(chosenTrainingValue.getTrainingName());
                    dataTV.setText(dt.getDate(WeekDayButton.this.trainingDate));
                    exerciseNumber.setText(getResources().getString(R.string.number_of_exercises)+" : " + chosenTrainingValue.getExerciseNumber());
                    timeExercise.setText(String.valueOf(chosenTrainingValue.getAverageTime()));
                } else  name.setText(getResources().getString(R.string.no_trainings));
                scrollLL.addView(trening);
                Timetable.parentLayout.addView(scrollView,2);
            }

        });

    }

    private void sprCzyJestTrening() {
        if(trainingValue !=null){
            isTraining = true;
        }
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTraining(boolean training) {
        this.isTraining = training;
    }

    public void init(){
        setSingleLine(true);

        if(currentMonth){

        } else{
            setTextColor(Color.parseColor("#a6a6a6"));
        }
        setText(String.valueOf(number));
        if(isTraining ==true){
            setBackgroundResource(R.drawable.button_week_days_pressed_calendar);
        } else{
            setBackgroundResource(R.drawable.button_week_days_calendar);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWidth(),getHeight());
        lp.setMargins(Units.dpToPx(getContext(),9),Units.dpToPx(getContext(),4),Units.dpToPx(getContext(),8),Units.dpToPx(getContext(),5));
        setTextSize(Units.dpToPx(getContext(),7));
        setPadding(Units.dpToPx(getContext(),3),Units.dpToPx(getContext(),3),Units.dpToPx(getContext(),3),Units.dpToPx(getContext(),3));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setLayoutParams(lp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(Units.dpToPx(getContext(),45));
        int parentHeight = MeasureSpec.getSize(Units.dpToPx(getContext(),45));
        this.setMeasuredDimension(parentWidth, parentHeight);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


    }
    public TrainingValue getTrainingValue() {
        return trainingValue;
    }


}
