package com.example.daniel.extraview;

/**
 * Created by Daniel on 2017-06-26.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.daniel.database.exercise.values.ExerciseValuesDatabase;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValuesDatabase;
import com.example.daniel.gymassistant.AddTraining;
import com.example.daniel.gymassistant.R;
import com.example.daniel.procedures.DateTraining;
import com.example.daniel.procedures.Units;
import com.example.daniel.values.AddTrainingValues;

import java.util.Date;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private TrainingValue[] trainingValues;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    public void setTrainingValues(TrainingValue[] trainingValues){
        this.trainingValues =trainingValues;
    }
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout layout = new LinearLayout(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        String[] weekDays = mContext.getResources().getStringArray(R.array.week_days);
        for(int i=0;i<7;i++){
            LinearLayout day = (LinearLayout) inflater.inflate(R.layout.timetable_week_list_day, null, false);
            ((TextView) day.findViewById(R.id.day_name)).setText(weekDays[i]);
            layout.addView(day,i);
        }
        setTrainings(layout, position);
        collection.addView(layout,lp);
        return layout;
    }
    private void setTrainings(LinearLayout layout, int position){
        String noTraining =mContext.getResources().getString(R.string.no_trainings);
        String[] weekDays = mContext.getResources().getStringArray(R.array.week_days);
        Date data= new Date();
        int dayNumber = data.getDay();
        if(dayNumber==0) dayNumber=6; else dayNumber--;
        Date day=null;
        Date monday=null;
        Date sunday=new Date(data.getYear(),data.getMonth(),data.getDate()- (data.getDay()==0 ? 6 : data.getDay()-1)+6+position*7);
        monday = new Date(data.getYear(),data.getMonth(),7*position+data.getDate()-(dayNumber));
        DateTraining trainingDate = new DateTraining(mContext);
        String[][] dates= new String[trainingValues.length][];
        for(int j = 0; j< trainingValues.length; j++) {
            dates[j] = trainingDate.getNearestDatesOfTrainings(trainingValues[j], position);
        }
        for(int i = 0;i<7;i++){
            TextView textView = new TextView(mContext);
            textView.setTextSize(Units.dpToPx(mContext,12));
            textView.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL);
            ((TextView)((LinearLayout)  layout.getChildAt(i)).getChildAt(0)).setGravity(Gravity.LEFT| Gravity.TOP);
            if(position==0){
                if(i==dayNumber){
                    day=data;
                }else if(i>dayNumber){
                    day= new Date(data.getYear(),data.getMonth(),data.getDate()+(i-dayNumber));
                } else{
                    day= new Date(data.getYear(),data.getMonth(),data.getDate()-(dayNumber-i));
                }
                ((TextView)((LinearLayout)  layout.getChildAt(i)).getChildAt(0)).setText(Html.fromHtml(weekDays[i]+"<br />"+trainingDate.getDate(day) ));
                if(day.before(new Date(data.getYear(),data.getMonth(),data.getDate()))) {
                    ((LinearLayout)  layout.getChildAt(i)).getChildAt(0).setBackgroundColor(Color.RED);
                } else if (day.before(new Date(data.getYear(),data.getMonth(),data.getDate()+1))&&!day.after(new Date(data.getYear(),data.getMonth(),data.getDate()+1))){
                    ((LinearLayout) layout.getChildAt(i)).getChildAt(0).setBackgroundColor(Color.BLUE);
                } else {
                    ((LinearLayout)  layout.getChildAt(i)).getChildAt(0).setBackgroundColor(Color.GREEN);
                }
            } else{
                if(i==dayNumber){
                    day= new Date(data.getYear(),data.getMonth(),data.getDate()+position*7);
                }else if(i>dayNumber){
                    day= new Date(data.getYear(),data.getMonth(),data.getDate()+(i-dayNumber)+position*7);
                } else{
                    day= new Date(data.getYear(),data.getMonth(),data.getDate()-(dayNumber-i)+position*7);
                }
                ((TextView)((LinearLayout) layout.getChildAt(i)).getChildAt(0)).setText(Html.fromHtml(weekDays[i]+"<br />"+trainingDate.getDate(day) ));
            }
            textView.setText(noTraining);
            for(int j = 0; j< trainingValues.length; j++){
                String help =trainingDate.getDate(monday);
                help = trainingDate.switchYearWithDay(help);
                help = removeZeroFromMonth(help);
                for(int k=0;k<dates[j].length;k++) {
                    if (dates[j][k].equals(help)) {
                        if (textView.getText().equals(noTraining)) {
                            textView.setText(trainingValues[j].getTrainingName());
                        } else {
                            textView.setText(textView.getText() + " / " + trainingValues[j].getTrainingName());
                        }
                    }
                }
            }
            monday = new Date(monday.getYear(),monday.getMonth(),monday.getDate()+1);
            ((LinearLayout)((ScrollView) ((LinearLayout) layout.getChildAt(i)).getChildAt(1)).getChildAt(0)).addView(textView);
            if(textView.getText().toString().equals(noTraining)){

            } else textView.setOnClickListener(setOnClickListener(textView.getText().toString()));
        }
    }

    private String removeZeroFromMonth(String date) {
        int numberOfDots=0;
        for (int i=0;i<date.length();i++){
            if(date.charAt(i)=='.') numberOfDots++;
            if(numberOfDots==0&&date.charAt(i)=='0'&&i==0) date= date.substring(0,i) + date.substring(i + 1);
            if(numberOfDots==1&&date.charAt(i)=='0') date= date.substring(0,i) + date.substring(i + 1);
        }
        return date;
    }
    private View.OnClickListener setOnClickListener(final String s) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseValuesDatabase wtd = new ExerciseValuesDatabase(mContext);
                TrainingValuesDatabase tvd = new TrainingValuesDatabase(mContext);
                AddTraining.openMode = AddTrainingValues.OPEN_FROM_SCHEDULE;
                Intent intent = new Intent(mContext, AddTraining.class);
                AddTraining.trainingValue = tvd.get(s);
                AddTraining.exerciseValues = wtd.get(s);
                AddTraining.defaultTrainingName = s;
                mContext.startActivity(intent);
            }
        };
        return listener;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



}
