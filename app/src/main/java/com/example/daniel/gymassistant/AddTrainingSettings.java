package com.example.daniel.gymassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.daniel.database.trainings.names.TrainingNamesDatabase;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValuesDatabase;
import com.example.daniel.procedures.DateTraining;
import com.example.daniel.values.AddTrainingValues;

import java.util.Date;
import java.util.Scanner;

public class AddTrainingSettings extends AppCompatActivity {

    LinearLayout parentLayout;
    Button[] weekDays = new Button[7];
    static String date ="";
    static String day ="";
    static String monthlyDate;
    Toolbar mActionBarToolbar;
    static boolean[] isWeekDayChosen = new boolean[7];
    {for (int i = 0; i< isWeekDayChosen.length; i++){
        isWeekDayChosen[i]=false;
    }
    }
    static TrainingValuesDatabase trainingValuesDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentLayout =(LinearLayout) View.inflate(this,R.layout.activity_add_training_settings,null);
        trainingValuesDatabase= new TrainingValuesDatabase(getApplicationContext());
        //LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, LinearLayout.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics()));
        setContentView(parentLayout);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_add_training_settings);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(AddTraining.defaultTrainingName);
        setCalendarListener();
        findWeekDaysButton();
        final Spinner repetitionSpinner = (Spinner) findViewById(R.id.repetition_slider);
        repetitionSpinner.setOnItemSelectedListener(setRepetitionSpinnerListener());
        switch(AddTraining.openMode){
            case AddTrainingValues.OPEN_FROM_SCHEDULE:
                setComponents();
                break;
        }
        RelativeLayout RL = (RelativeLayout) View.inflate(this, R.layout.add_training_settings_check,null);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageView buttonCheck = (ImageView) RL.findViewById(R.id.image_check);
        buttonCheck.setOnClickListener(setOnClickListenerCheck());
        addContentView(RL,rp);


    }

    private void setComponents() {
        TrainingValue trainingValue = trainingValuesDatabase.get(AddTraining.mActionBarToolbar.getTitle().toString());
        int roundsNumber = Integer.valueOf(trainingValue.getRoundsNumber());
        roundsNumber--;
        ((Spinner) findViewById(R.id.round_number_spinner)).setSelection(roundsNumber);
        String schedule= trainingValue.getSchedule();
        String[] schedules = getResources().getStringArray(R.array.repetition);
        int number=0;
        for(int i=0;i<schedules.length;i++) {
            if (schedule.equals(schedules[i])) {
                number = i;
                break;
            }
        }
        ((Spinner) findViewById(R.id.repetition_slider)).setSelection(number);
        if(number==0||number==1){
            String days = trainingValue.getWeekDays();
            String[] weekDaysArray =getResources().getStringArray(R.array.short_week_days);
            Scanner s = new Scanner(days);
            for(int i=0;s.hasNext();i++) {
                String day=s.next();
                for(int j=0;j<weekDaysArray.length;j++){
                    if(weekDaysArray[j].equals(day)){
                        weekDays[j].setSelected(true);
                        isWeekDayChosen[i]=true;
                        break;
                    }
                }
            }
        } else if(number == 2){
            DateTraining dt= new DateTraining(this);
            ((CalendarView) findViewById(R.id.calendar_view)).setDate(dt.readDateFromString(trainingValue.getFirstDayTraining()).getTime());
        } else{
            ((Spinner) findViewById(R.id.slider_repetition_2)).setSelection(trainingValue.getRepetition()-1);
        }

    }

    @Override
    protected void onStop(){
        super.onStop();
        for(int i = 0; i< isWeekDayChosen.length; i++){
            isWeekDayChosen[i]=false;
        }
    }
    private   View.OnClickListener setOnClickListenerCheck(){
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                savingData();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        };
        return listener;
    }
    private void savingData(){
        AddTraining.saveData();
        String weekDaysString= "", trainingMode, schedule;
        int roundNumber = 1, repetition=0;
        String[] shortDaysWeek = getResources().getStringArray(R.array.short_week_days);
        if(isWeekDayChosen[0]==true){weekDaysString = weekDaysString+shortDaysWeek[0]+" ";}
        if(isWeekDayChosen[1]==true){weekDaysString = weekDaysString+shortDaysWeek[1]+" ";}
        if(isWeekDayChosen[2]==true){weekDaysString = weekDaysString+shortDaysWeek[2]+" ";}
        if(isWeekDayChosen[3]==true){weekDaysString = weekDaysString+shortDaysWeek[3]+" ";}
        if(isWeekDayChosen[4]==true){weekDaysString = weekDaysString+shortDaysWeek[4]+" ";}
        if(isWeekDayChosen[5]==true){weekDaysString = weekDaysString+shortDaysWeek[5]+" ";}
        if(isWeekDayChosen[6]==true){weekDaysString = weekDaysString+shortDaysWeek[6]+" ";}
        Spinner roundsNumberSpinner = findViewById(R.id.round_number_spinner);
        Spinner trainingModeSpinner = findViewById(R.id.training_mode_spinner);
        Spinner repetitionSpinner = findViewById(R.id.repetition_slider);
        Spinner repetitionSpinner2 = findViewById(R.id.slider_repetition_2);
        roundNumber = Integer.valueOf(roundsNumberSpinner.getSelectedItem().toString());
        trainingMode = trainingModeSpinner.getSelectedItem().toString();
        schedule = repetitionSpinner.getSelectedItem().toString();
        repetition = Integer.valueOf(repetitionSpinner2.getSelectedItem().toString());
        Date today = new Date();
        String data = today.getDate()+"."+today.getMonth()+"."+(today.getYear()+1900);
        TrainingValue wt;
        TrainingNamesDatabase tnd = new TrainingNamesDatabase(getApplicationContext());
        DateTraining dt = new DateTraining(this);
        //TODO in case someone is switching spinner
        if(!(weekDaysString==null)&&!(weekDaysString=="")){
            wt = new TrainingValue(tnd.getIndex(AddTraining.defaultTrainingName),weekDaysString,trainingMode,schedule,roundNumber,AddTraining.numberOfExercises,data,"" ,"",repetition,0);
        } else if(today!=null){
            wt = new TrainingValue(tnd.getIndex(AddTraining.defaultTrainingName), day,trainingMode,schedule,roundNumber,AddTraining.numberOfExercises,data, date,date,repetition,0);
        } else {
            wt = new TrainingValue(tnd.getIndex(AddTraining.defaultTrainingName), day,trainingMode,schedule,roundNumber,AddTraining.numberOfExercises,data, "","",repetition,0);

        }
        switch(AddTraining.openMode) {
            case AddTrainingValues.OPEN_FROM_MAIN_MENU: trainingValuesDatabase.add(wt); break;
            case AddTrainingValues.OPEN_FROM_SCHEDULE:  trainingValuesDatabase.add(wt); break;
            case AddTrainingValues.OPEN_FROM_PROGRESS:  trainingValuesDatabase.add(wt);break;
        }
        (new DateTraining(getApplicationContext())).getNearestTrainingDate(wt);
        AddTraining.exerciseValuesList.clear();
        AddTraining.exercises.clear();
    }
    private void setCalendarListener(){
        final CalendarView.OnDateChangeListener myCalendarListener = new CalendarView.OnDateChangeListener(){

            public void onSelectedDayChange(CalendarView view, int year, int month, int day){
                month = month + 1;
                date = year+"."+month+"."+ day;
                Date data = new Date(year-1900,month-1,day);
                String[] dniTyg= getResources().getStringArray(R.array.short_week_days);
                switch (data.getDay()){
                    case 0: AddTrainingSettings.day =dniTyg[6];
                        break;
                    case 1: AddTrainingSettings.day =dniTyg[0];
                        break;
                    case 2: AddTrainingSettings.day =dniTyg[1];
                        break;
                    case 3: AddTrainingSettings.day =dniTyg[2];
                        break;
                    case 4: AddTrainingSettings.day =dniTyg[3];
                        break;
                    case 5: AddTrainingSettings.day =dniTyg[4];
                        break;
                    case 6: AddTrainingSettings.day =dniTyg[5];
                        break;
                }
                DateTraining dateTraining = new DateTraining(getApplicationContext());
                AddTrainingSettings.date = dateTraining.getDate(data);

            }
        };
        CalendarView calendarView = findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(myCalendarListener);

    }
    private AdapterView.OnItemSelectedListener setRepetitionSpinnerListener(){
        AdapterView.OnItemSelectedListener AV = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner repetitionNumberOfDays =findViewById(R.id.slider_repetition_2);
                TextView chooseDay =findViewById(R.id.choose_day);
                LinearLayout weekDays = findViewById(R.id.week_days_linear_layout);
                CalendarView calendarView = findViewById(R.id.calendar_view);
                if(position==3){
                    repetitionNumberOfDays.setVisibility(View.VISIBLE);
                    chooseDay.setVisibility(View.GONE);
                    weekDays.setVisibility(View.GONE);
                    calendarView.setVisibility(View.VISIBLE);
                }
                else if(position==2){
                    repetitionNumberOfDays.setVisibility(View.GONE);
                    chooseDay.setVisibility(View.GONE);
                    weekDays.setVisibility(View.GONE);
                    calendarView.setVisibility(View.VISIBLE);
                } else{
                    repetitionNumberOfDays.setVisibility(View.GONE);
                    chooseDay.setVisibility(View.VISIBLE);
                    weekDays.setVisibility(View.VISIBLE);
                    calendarView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        };
        return  AV;
    }


    private void findWeekDaysButton(){
        weekDays[0] = findViewById(R.id.mon);
        weekDays[1] = findViewById(R.id.tue);
        weekDays[2] = findViewById(R.id.wed);
        weekDays[3] = findViewById(R.id.thu);
        weekDays[4] = findViewById(R.id.fri);
        weekDays[5] = findViewById(R.id.sat);
        weekDays[6] = findViewById(R.id.sun);
        setActionListenerWeekDays();
    }
    private void setActionListenerWeekDays(){
        for(int i=0;i<7;i++){
            weekDays[i].setOnClickListener(chooseWeekDayButton(i, weekDays[i]));
        }
    }
    private  View.OnClickListener chooseWeekDayButton(final int number, final Button button){
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isWeekDayChosen[number]==true){
                    button.setBackground(getResources().getDrawable(R.drawable.button_week_days));
                    isWeekDayChosen[number]=false;
                } else{
                    button.setBackground(getResources().getDrawable(R.drawable.button_week_days_pressed));
                    isWeekDayChosen[number]=true;
                }
            }
        };
        return listener;
    }
}
