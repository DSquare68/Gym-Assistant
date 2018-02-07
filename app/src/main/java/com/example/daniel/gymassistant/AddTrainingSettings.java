package com.example.daniel.gymassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.daniel.database.trainings.names.TrainingNamesDatebase;
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
        ustawKalendarzLisener();
        znajdźDniTygodniaButton();
        final Spinner powtarzanie = (Spinner) findViewById(R.id.repetition_slider);
        powtarzanie.setOnItemSelectedListener(ustawPowtarzenieSpinnerLisener());
        switch(AddTraining.openMode){
            case AddTrainingValues.OPEN_FROM_SCHEDULE:
                ustawKompozytyWedłógDanych();
                break;
        }
        RelativeLayout RL = (RelativeLayout) View.inflate(this, R.layout.add_training_settings_check,null);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ImageView przyciskCheck = (ImageView) RL.findViewById(R.id.image_check);
        przyciskCheck.setOnClickListener(ustawOnClickLitsenerCheck());
        addContentView(RL,rp);


    }

    private void ustawKompozytyWedłógDanych() {
        TrainingValue właściwościTreningów = trainingValuesDatabase.get(AddTraining.mActionBarToolbar.getTitle().toString());
        int roundsNumber = Integer.valueOf(właściwościTreningów.getRoundsNumber());
        roundsNumber--;
        ((Spinner) findViewById(R.id.round_number_spinner)).setSelection(roundsNumber);
        String schedule= właściwościTreningów.getSchedule();
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
            String days = właściwościTreningów.getWeekDays();
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
            ((CalendarView) findViewById(R.id.calendar_view)).setDate(dt.readDateFromString(właściwościTreningów.getFirstDayTraining()).getTime());
        } else{
            ((Spinner) findViewById(R.id.slider_repetition_2)).setSelection(właściwościTreningów.getRepetition()-1);
        }

    }

    @Override
    protected void onStop(){
        super.onStop();
        for(int i = 0; i< isWeekDayChosen.length; i++){
            isWeekDayChosen[i]=false;
        }
    }
    private   View.OnClickListener ustawOnClickLitsenerCheck(){
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                końcowaCzynność();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        };
        return listener;
    }

    private void edytujTrening() {

    }

    private void końcowaCzynność(){
        AddTraining.saveData();
        String weekDaysString= "", trainingMode, schedule;
        int roundNumber = 1, repetition=0;

        if(isWeekDayChosen[0]==true){weekDaysString = weekDaysString+"pon ";}
        if(isWeekDayChosen[1]==true){weekDaysString = weekDaysString+"wt ";}
        if(isWeekDayChosen[2]==true){weekDaysString = weekDaysString+"śr ";}
        if(isWeekDayChosen[3]==true){weekDaysString = weekDaysString+"czw ";}
        if(isWeekDayChosen[4]==true){weekDaysString = weekDaysString+"pt ";}
        if(isWeekDayChosen[5]==true){weekDaysString = weekDaysString+"sob ";}
        if(isWeekDayChosen[6]==true){weekDaysString = weekDaysString+"ndz ";}
        Spinner ilośćSeriiSpinner = (Spinner) findViewById(R.id.round_number_spinner);
        Spinner trybTrenignuSpinner = (Spinner) findViewById(R.id.training_mode_spinner);
        Spinner powtarzanieSpinner = (Spinner) findViewById(R.id.repetition_slider);
        Spinner powtarzanieSpinner2 = (Spinner) findViewById(R.id.slider_repetition_2);
        roundNumber = Integer.valueOf(ilośćSeriiSpinner.getSelectedItem().toString());
        trainingMode = trybTrenignuSpinner.getSelectedItem().toString();
        schedule = powtarzanieSpinner.getSelectedItem().toString();
        repetition = Integer.valueOf(powtarzanieSpinner2.getSelectedItem().toString());
        Date date = new Date();
        String data = date.getDate()+"."+date.getMonth()+"."+(date.getYear()+1900);
        TrainingValue wt;
        TrainingNamesDatebase tnd = new TrainingNamesDatebase(getApplicationContext());
        DateTraining dt = new DateTraining(this);
        if(AddTrainingSettings.date.equals(null)|| AddTrainingSettings.date.equals("")) AddTrainingSettings.date =dt.readDate(weekDaysString,schedule);
        if(!(weekDaysString==null)&&!(weekDaysString=="")){
            wt = new TrainingValue(tnd.getIndex(AddTraining.defaultTrainingName),weekDaysString,trainingMode,schedule,roundNumber,AddTraining.numberOfExercises,data, AddTrainingSettings.date,"",repetition,0);
        } else{
            wt = new TrainingValue(tnd.getIndex(AddTraining.defaultTrainingName), day,trainingMode,schedule,roundNumber,AddTraining.numberOfExercises,data, AddTrainingSettings.date,"",repetition,0);
        }
        switch(AddTraining.openMode) {
            case AddTrainingValues.OPEN_FROM_MAIN_MENU: trainingValuesDatabase.add(wt); break;
            case AddTrainingValues.OPEN_FROM_SCHEDULE:  trainingValuesDatabase.add(wt); break;
            case AddTrainingValues.OPEN_FROM_PROGRESS:  trainingValuesDatabase.add(wt);break;
        }
        AddTraining.exerciseValuesList.clear();
        AddTraining.exercises.clear();
        AddTraining.numberOfExercises=0;
    }
    private void ustawKalendarzLisener(){
        final CalendarView.OnDateChangeListener myCalendarListener = new CalendarView.OnDateChangeListener(){

            public void onSelectedDayChange(CalendarView view, int year, int month, int day){
                month = month + 1;
                Log.d("rok",String.valueOf(day)+"  "+String.valueOf(month)+"  "+String.valueOf(year));
                date = year+"."+month+"."+ day;
                Date data = new Date(year-1900,month-1,day);
                String[] dniTyg={"pon","wt","śr","czw","pt","sob","ndz"};
                Log.d("dzień",String.valueOf(data.getDay()));
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

            }
        };
        CalendarView kalendarz = findViewById(R.id.calendar_view);
        kalendarz.setOnDateChangeListener(myCalendarListener);

    }
    private AdapterView.OnItemSelectedListener ustawPowtarzenieSpinnerLisener(){
        AdapterView.OnItemSelectedListener AV = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner powtarzanieIlośćDni =findViewById(R.id.slider_repetition_2);
                TextView wybierzDni =findViewById(R.id.choose_day);
                LinearLayout dniTygodniLL = findViewById(R.id.week_days_linear_layout);
                CalendarView kalendarz = findViewById(R.id.calendar_view);
                if(position==3){
                    powtarzanieIlośćDni.setVisibility(View.VISIBLE);
                    wybierzDni.setVisibility(View.GONE);
                    dniTygodniLL.setVisibility(View.GONE);
                    kalendarz.setVisibility(View.VISIBLE);
                }
                else if(position==2){
                    powtarzanieIlośćDni.setVisibility(View.GONE);
                    wybierzDni.setVisibility(View.GONE);
                    dniTygodniLL.setVisibility(View.GONE);
                    kalendarz.setVisibility(View.VISIBLE);
                } else{
                    powtarzanieIlośćDni.setVisibility(View.GONE);
                    wybierzDni.setVisibility(View.VISIBLE);
                    dniTygodniLL.setVisibility(View.VISIBLE);
                    kalendarz.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        };
        return  AV;
    }


    private void znajdźDniTygodniaButton(){
        weekDays[0] = findViewById(R.id.mon);
        weekDays[1] = findViewById(R.id.tue);
        weekDays[2] = findViewById(R.id.wed);
        weekDays[3] = findViewById(R.id.thu);
        weekDays[4] = findViewById(R.id.fri);
        weekDays[5] = findViewById(R.id.sat);
        weekDays[6] = findViewById(R.id.sun);
        ustawActionLisenerDniTygodnia();
    }
    private void ustawActionLisenerDniTygodnia(){
        for(int i=0;i<7;i++){
            weekDays[i].setOnClickListener(wybierzDzieńTygodniaButton(i, weekDays[i]));
        }
    }
    private  View.OnClickListener wybierzDzieńTygodniaButton(final int numer, final Button button){
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isWeekDayChosen[numer]==true){
                    button.setBackground(getResources().getDrawable(R.drawable.button_week_days));
                    isWeekDayChosen[numer]=false;
                } else{
                    button.setBackground(getResources().getDrawable(R.drawable.button_week_days_pressed));
                    isWeekDayChosen[numer]=true;
                }
            }
        };
        return listener;
    }
}
