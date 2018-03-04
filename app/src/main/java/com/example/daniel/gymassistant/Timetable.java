package com.example.daniel.gymassistant;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.daniel.database.dataoldtrainings.OldTrainingsDatabase;
import com.example.daniel.database.exercise.values.ExerciseValuesDatabase;
import com.example.daniel.database.trainings.names.TrainingName;
import com.example.daniel.database.trainings.names.TrainingNamesDatabase;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValuesDatabase;
import com.example.daniel.extraview.CalendarViewNew;
import com.example.daniel.extraview.CustomPagerAdapter;
import com.example.daniel.procedures.DateTraining;
import com.example.daniel.values.AddTrainingValues;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Timetable extends AppCompatActivity {
    static ArrayList<LinearLayout> trainings = new ArrayList<>();
    static TrainingName[] trainingNames;
    LinearLayout trainingLinearLayout;
    static public LinearLayout parentLayout;
    LinearLayout containerTraining;
    LinearLayout containerTrainings;
    ScrollView trainingScrollView;
    Toolbar mActionBarToolbar;
    static Context context;
    DateTraining dateTraining;
 
    static int showMode =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        dateTraining = new DateTraining(this);
        parentLayout =(LinearLayout) View.inflate(this,R.layout.activity_timetable,null);
        context = parentLayout.getContext();
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        mActionBarToolbar = parentLayout.findViewById(R.id.toolbar_timetable);
        setSupportActionBar(mActionBarToolbar);
        switch (showMode){
            case 1:
                showList();
                break;
            case 2:
                showList();
                break;
            case 3:
                showWeekly();
                break;
            case 4:
                showMonthly();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timetable, menu);
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                for(int i = 0; i< trainings.size(); i++){
                    ImageView delete = (ImageView) trainings.get(i).getChildAt(1);
                    LinearLayout.LayoutParams lp =(LinearLayout.LayoutParams) delete.getLayoutParams();
                    if(lp.weight!=1){
                        delete.setOnClickListener(setOnClickListenerImageViewDelete(i)); //dodać DAneStarychTreningów
                    } else{

                    }
                    if(lp.weight!=1){
                        lp.weight=1;
                    } else {
                        lp.weight=-1;
                    }
                    //  Poprawidź widoczność Text view dla daty, i innych
                    delete.setLayoutParams(lp);
                }
                return false;
            }

            private View.OnClickListener setOnClickListenerImageViewDelete(final int i) {
                View.OnClickListener clickLisener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TrainingNamesDatabase nt = new TrainingNamesDatabase(context);
                        ExerciseValuesDatabase wc = new ExerciseValuesDatabase(context);
                        TrainingValuesDatabase wt = new TrainingValuesDatabase(context);
                        OldTrainingsDatabase otd = new OldTrainingsDatabase(context,null);

                        nt.deleteTrainingName(trainingNames[i].getID());
                        wc.delate(trainingNames[i].getID());
                        wt.deleteTrainingValueByTrainingID(trainingNames[i].getID());
                        otd.deleteAll();

                        LinearLayout ll =(LinearLayout) v.getParent();
                        containerTrainings.removeView(ll);
                        containerTrainings.invalidate();
                    }

                };
                return clickLisener;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                TrainingNamesDatabase nt = new TrainingNamesDatabase(context);
                ExerciseValuesDatabase wc = new ExerciseValuesDatabase(context);
                TrainingValuesDatabase wt = new TrainingValuesDatabase(context);
                TrainingName[] trainingNames =nt.getAll();
                for(int i=0;i<nt.getCount();i++) {
                    OldTrainingsDatabase dst = new OldTrainingsDatabase(context, trainingNames[i].getName());
                    dst.deleteAll();
                }

                nt.deleteAll();
                wc.delateAll();
                wt.delateAll();
                containerTrainings.removeAllViews();
                containerTrainings.invalidate();
                return false;
            }
        });
        menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { //sortowanie według
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return true;
            }
        });
        menu.getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { //sortowanie według
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createShowModeDialog();
                return true;
            }

            private void createShowModeDialog() {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_radio_group_cancel_ok);
                dialog.setTitle(getResources().getString(R.string.view_mode));
                final RadioGroup radioGroup = dialog.findViewById(R.id.radio_group_start_mode);
                String[] s= getResources().getStringArray(R.array.timetable_view_mode);
                final RadioButton[] radioButtons = new RadioButton[s.length];
                for(int i=0;i<s.length;i++){
                    radioButtons[i] = new RadioButton(context);
                    radioButtons[i].setText(s[i]);
                    radioGroup.addView(radioButtons[i]);
                }
                Button okButton =  dialog.findViewById(R.id.ok);
                Button cancelButton= dialog.findViewById(R.id.cancel);
                okButton.setOnClickListener(okButtonActionListenerShowMode(radioGroup, dialog));

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Timetable.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }

            private View.OnClickListener okButtonActionListenerShowMode(final RadioGroup radioGroup, final Dialog dialog) {
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        if(radioButtonID!=-1) {
                            View radioButton = radioGroup.findViewById(radioButtonID);
                            int idx = radioGroup.indexOfChild(radioButton);
                            switch(idx){
                                case 0:
                                    showMode =1;
                                    break;
                                case 1:
                                    showMode =2;
                                    break;
                                case 2:
                                    showMode =3;
                                    break;
                                case 3:
                                    showMode =4;
                                    break;
                            }
                            dialog.dismiss();
                            finish();
                            startActivity(getIntent()
                            );
                        }

                    }
                };
                return listener;
            }
        });
        return true;
    }
    private void showList() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, LinearLayout.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics()));
        setScrollView();
        trainingScrollView.setLayoutParams(lp);
        lp.height=LinearLayout.LayoutParams.FILL_PARENT;
        trainingScrollView.addView(containerTrainings,lp);

        LinearLayout.LayoutParams lpg = ( LinearLayout.LayoutParams) trainingScrollView.getLayoutParams();
        lpg.gravity= Gravity.NO_GRAVITY;
        addContentView(parentLayout,lp);
    }


    private void showWeekly() {
        TrainingValuesDatabase ntd = new TrainingValuesDatabase(context);
        TrainingValue[] trainingValues = ntd.getAll();
        ViewPager viewPager = parentLayout.findViewById(R.id.viewpager);
        viewPager.setVisibility(View.VISIBLE);
        CustomPagerAdapter cpa = new CustomPagerAdapter(context);
        cpa.setTrainingValues(trainingValues);
        viewPager.setAdapter(cpa);
        LinearLayout LL = parentLayout.findViewById(R.id.linearLayout_timetable);
        LL.setVisibility(View.GONE);
        setContentView(parentLayout);
    }

    private void showMonthly() {
        ViewPager viewPager =  parentLayout.findViewById(R.id.viewpager);
        viewPager.setVisibility(View.GONE);
        LinearLayout LL = parentLayout.findViewById(R.id.linearLayout_timetable);
        LL.setVisibility(View.GONE);
        CalendarViewNew calendar = new CalendarViewNew(this);
        parentLayout.addView(calendar,1);
        setContentView(parentLayout);
    }

    private void setScrollView() {
        containerTraining =(LinearLayout) View.inflate(this,R.layout.timetable_training,null);
        TrainingNamesDatabase ntd = new TrainingNamesDatabase(this);
        TrainingValuesDatabase wtd = new TrainingValuesDatabase(this);
        TrainingValue[] trainingValues = wtd.getAll();
        trainingLinearLayout = containerTraining.findViewById(R.id.linearLayout_timetable_list);
        LinearLayout ll2 =(LinearLayout) parentLayout.getChildAt(1);
        trainingScrollView = (ScrollView) ll2.getChildAt(0);
        trainingNames = ntd.getAll();
        containerTrainings = new LinearLayout(getApplicationContext());
        containerTrainings.setOrientation(LinearLayout.VERTICAL);
        LinearLayout ll =(LinearLayout) trainingLinearLayout.getParent();
        ll.removeView(trainingLinearLayout);
        for(int i=0;i<wtd.getCount();i++) {
            trainings.add(i, (LinearLayout) getLayoutInflater().inflate(R.layout.timetable_training, parentLayout, false));
            LinearLayout LL =(LinearLayout) trainings.get(i).getChildAt(0);
            TextView name =LL.findViewById(R.id.training_name);
            TextView date =LL.findViewById(R.id.date_of_training);
            TextView exercisesNumber =LL.findViewById(R.id.number_of_exercises);
            TextView time = LL.findViewById(R.id.time);
            name.setText(trainingNames[i].getName());
            date.setText(dateTraining.getNearestTrainingDate(trainingValues[i]));
            exercisesNumber.setText(getResources().getString(R.string.number_of_exercises)+": "+trainingValues[i].getExerciseNumber());
            Date timeD = new Date(trainingValues[i].getAverageTime());
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            time.setText(String.valueOf(formatter.format(timeD)));
            trainings.get(i).setOnClickListener(setOnClickListener(i));
        }
        int[] nr = new int[wtd.getCount()];
        for(int i=0;i<wtd.getCount();i++){
            nr[i]=i;
        }
        switch(showMode){
            case 1:
                sortAlphabetically(nr, trainingNames);
                break;
            case 2:
                sortChronological(nr, trainingValues);
                break;
        }
        for(int i=0; i<wtd.getCount();i++){
            containerTrainings.addView(trainings.get(nr[i]));
        }
    }
    private View.OnClickListener setOnClickListener(final int i) {
        View.OnClickListener cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTraining(i);
            }
        };
        return cl;
    }

    private void showTraining(int i) {
        ExerciseValuesDatabase wtd = new ExerciseValuesDatabase(this);
        TrainingValuesDatabase wtrend = new TrainingValuesDatabase(this);
        AddTraining.trainingValue = wtrend.getByTrainingID(trainingNames[i].getID());
        AddTraining.exerciseValues = wtd.get(trainingNames[i].getName());
        AddTraining.defaultTrainingName = trainingNames[i].getName();
        AddTraining.openMode = AddTrainingValues.OPEN_FROM_SCHEDULE;
        Intent intent = new Intent(this, AddTraining.class);
        startActivity(intent);
    }
    private void sortChronological(int[] numer, TrainingValue[] właściwości) {
        TrainingValue tempStr;
        int tempInt;
        for(int j=0;j<numer.length;j++) {
            for (int i = 0; i < numer.length - j - 1; i++) {
                if (dateTraining.readDateFromString(dateTraining.getNearestTrainingDate( właściwości[i + 1])).before(
                        dateTraining.readDateFromString(dateTraining.getNearestTrainingDate( właściwości[i]))
                )) {
                    tempStr = właściwości[i];
                    właściwości[i] = właściwości[i + 1];
                    właściwości[i + 1] = tempStr;

                    tempInt =numer[i];
                    numer[i]=numer[i+1];
                    numer[i+1]=tempInt;
                }
            }
        }
    }

    private void sortAlphabetically(int[] numer, TrainingName[] nazwy) {
        TrainingName tempStr;
        int tempInt;
        for(int j=0;j<numer.length;j++){
            for (int i= 0; i < numer.length - j -1; i++) {
                if(nazwy[i+1].getName().compareTo(nazwy[i].getName())<0) {
                    tempStr = nazwy[i];
                    nazwy[i] = nazwy[i + 1];
                    nazwy[i + 1] = tempStr;

                    tempInt =numer[i];
                    numer[i]=numer[i+1];
                    numer[i+1]=tempInt;
                }
            }

        }
    }
}

