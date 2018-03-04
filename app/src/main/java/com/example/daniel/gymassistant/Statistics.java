package com.example.daniel.gymassistant;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.database.dataoldtrainings.OldTraining;
import com.example.daniel.database.dataoldtrainings.OldTrainingsDatabase;
import com.example.daniel.database.exercise.name.Exercise;
import com.example.daniel.database.exercise.name.ExerciseDatabase;
import com.example.daniel.database.trainings.names.TrainingName;
import com.example.daniel.database.trainings.names.TrainingNamesDatabase;
import com.example.daniel.extraview.StatisticsExerciseAdapter;
import com.example.daniel.procedures.DateTraining;

import java.util.ArrayList;

import static com.example.daniel.database.exercise.name.DatebaseOfexerciseNames.nazwyTreningów;

public class Statistics extends AppCompatActivity {
    LinearLayout parentLayout, mainContainer;
    ScrollView mainScrollView;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<LinearLayout> namesLinearLayout = new ArrayList<>();
    boolean[] isSliderTurnOn;
    static public Exercise[] allExercises;
    static public int showMode =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentLayout = (LinearLayout) View.inflate(this,R.layout.activity_statistics,null);
        toolbar = parentLayout.findViewById(R.id.toolbar_statistics);
        setSupportActionBar(toolbar);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        switch(showMode) {
            case 0: setTrainingsList(); break;
            case 1: setExerciseList(); break;
        }
        setContentView(parentLayout,lp);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_statistics, menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setDialogShowMode();
                return false;
            }
        });
        return true;
    }

    private void setDialogShowMode() {
        Dialog dialog = new Dialog(Statistics.this);
        dialog.setContentView(R.layout.dialog_radio_group_cancel_ok);
        dialog.setTitle(getResources().getString(R.string.view_mode));
        final RadioGroup radioGroup =  dialog.findViewById(R.id.radio_group_start_mode);
        String[] s= getResources().getStringArray(R.array.statistics_show_mode);
        final RadioButton[] radioButtons = new RadioButton[s.length];
        for(int i=0;i<s.length;i++){
            radioButtons[i] = new RadioButton(getApplicationContext());
            radioButtons[i].setText(s[i]);
            radioGroup.addView(radioButtons[i]);
        }
        Button okButton =  dialog.findViewById(R.id.ok);
        Button anulujButton= dialog.findViewById(R.id.cancel);
        okButton.setOnClickListener(okButtonActionListenerShowMode(radioGroup, dialog));

        anulujButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Statistics.this, MainActivity.class);
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
                            showMode =0;
                            break;
                        case 1:
                            showMode =1;
                            break;
                    }
                    dialog.dismiss();
                    finish();
                    startActivity(getIntent());
                }

            }
        };
        return listener;
    }

    private void setExerciseList() {
        ExerciseDatabase wcd = new ExerciseDatabase(this);
        allExercises = wcd.getAll();
        sort(nazwyTreningów,0, nazwyTreningów.length-1);
        ArrayList<Exercise> listData = new ArrayList();
        for (int i = 0; i< nazwyTreningów.length; i++){
            listData.add(i, new Exercise(nazwyTreningów[i],getApplicationContext()));
        }
        StatisticsExerciseAdapter adapter = new StatisticsExerciseAdapter(listData, this);
        mainContainer =(LinearLayout) View.inflate(this,R.layout.statistics_list,null);
        recyclerView =(RecyclerView) mainContainer.findViewById(R.id.statistics_list_exercise_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setScrollContainer(true);
        recyclerView.setAdapter(adapter);
        toolbar.setTitle(getResources().getString(R.string.exercise));
        //setContentView(mainContainer);

        parentLayout.addView(mainContainer);
    }

    private void sort(String[] trainingNames, int x, int y) {
        int i,j;
        String v,temp;

        i=x;
        j=y;
        v=trainingNames[(x+y) / 2];
        do {
            while (trainingNames[i].compareTo(v)<0)
                i++;
            while (trainingNames[j].compareTo(v)>0)
                j--;
            if (i<=j) {
                temp=trainingNames[i];
                trainingNames[i]=trainingNames[j];
                trainingNames[j]=temp;
                i++;
                j--;
            }
        }
        while (i<=j);
        if (x<j)
            sort(trainingNames,x,j);
        if (i<y)
            sort(trainingNames,i,y);
    }

    private void setTrainingsList() {
        mainContainer =(LinearLayout) View.inflate(this,R.layout.statistics_position_of_training,null);
        mainScrollView =  mainContainer.findViewById(R.id.statistics_scroll_view_names);
        LinearLayout LL = (LinearLayout) mainScrollView.getChildAt(0);
        TrainingNamesDatabase ntd = new TrainingNamesDatabase(this);
        TrainingName[] trainingNames = ntd.getAll();
        isSliderTurnOn = new boolean[ntd.getCount()];

        for(int i=0;i<trainingNames.length;i++){
            LinearLayout LL2 = (LinearLayout) View.inflate(this,R.layout.statistics_position_of_training,null);
            namesLinearLayout.add(i,(LinearLayout) LL2.findViewById(R.id.statistics_container_name));
            ((LinearLayout) namesLinearLayout.get(i).getParent()).removeView(namesLinearLayout.get(i));
            TextView name = namesLinearLayout.get(i).findViewById(R.id.statistics_name_of_training_choose);
            ImageView slider = namesLinearLayout.get(i).findViewById(R.id.slide_button);
            slider.setOnClickListener(ustawSuwakOnClickLisener(trainingNames[i].getName(),i));
            name.setText(trainingNames[i].getName());
            LL.addView(namesLinearLayout.get(i));
        }
        ((LinearLayout) mainScrollView.getParent()).removeView(mainScrollView);
        parentLayout.addView(mainScrollView);
    }

    private View.OnClickListener ustawSuwakOnClickLisener(final String trainingName,final int i) {
        View.OnClickListener lisener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTraining dateTraining = new DateTraining(Statistics.this);
                if(!isSliderTurnOn[i]) {
                    isSliderTurnOn[i]=true;
                    mainContainer = (LinearLayout) View.inflate(Statistics.this, R.layout.statistics_position_of_training, null);
                    ScrollView dateAndTimeScroll= mainContainer.findViewById(R.id.statistics_scroll_view_on_date_and_time);;
                    LinearLayout  LL = (LinearLayout) v.getParent();
                    OldTrainingsDatabase dstd = new OldTrainingsDatabase(Statistics.this, trainingName);
                    dateAndTimeScroll.setVisibility(View.VISIBLE);
                    ArrayList<LinearLayout> dateAndTimeArrayLL = new ArrayList<>();
                    String[][] dateAndTime = dstd.getDateAndTime();
                    if(dateAndTime==null){
                        Toast toast = Toast.makeText(Statistics.this,getResources().getString(R.string.no_trainings),Toast.LENGTH_SHORT);
                        toast.show();
                        isSliderTurnOn[i]=false;
                        return;
                    }
                    for(int i=0;i<dateAndTime.length;i++){
                        dateAndTime[i][0]=dateTraining.switchYearWithDay(dateAndTime[i][0]);
                    }
                    for (int i = 0; i < dateAndTime.length; i++) {
                        LinearLayout całyKontener = (LinearLayout) View.inflate(Statistics.this, R.layout.statistics_position_of_training, null);
                        dateAndTimeArrayLL.add(i, (LinearLayout) całyKontener.findViewById(R.id.container_date_and_time_2));
                        ((LinearLayout) dateAndTimeArrayLL.get(i).getParent()).removeView(dateAndTimeArrayLL.get(i));
                        TextView data =  dateAndTimeArrayLL.get(i).findViewById(R.id.date_chooser);
                        TextView czas =  dateAndTimeArrayLL.get(i).findViewById(R.id.time_chooser);
                        data.setText(dateAndTime[i][0]);
                        czas.setText(dateAndTime[i][1]);
                        ((LinearLayout) dateAndTimeScroll.getChildAt(0)).addView(dateAndTimeArrayLL.get(i),i);
                    }
                    ((LinearLayout) dateAndTimeScroll.getParent()).removeView(dateAndTimeScroll);
                    ((LinearLayout) LL.getParent()).addView(dateAndTimeScroll,1);
                    for(int i=0;i<dateAndTime.length;i++){
                        dateAndTime[i][0]=dateTraining.switchYearWithDay(dateAndTime[i][0]);
                    }
                    for(int i=0;i<dateAndTimeArrayLL.size();i++){
                        dateAndTimeArrayLL.get(i).setOnClickListener(setOpenTrainingResultOnClickListener(dstd.getTrainingFromTimeAndDate((dateAndTime[i][0]),dateAndTime[i][2]), dateTraining.lastTraining(dateAndTime[i][0],dateAndTime[i][2],trainingName,getApplicationContext())));
                    }
                } else {
                    isSliderTurnOn[i]=false;
                    //((LinearLayout) LL.getParent()).removeView(dataICzasScroll);
                    ((LinearLayout)(  v.getParent()).getParent()).removeViewAt(1);

                }
            }

            private int sumSerii(OldTraining[] dane, int koniec) {
                int sum=0, seria=1, nrSerii=0;
                for(int i=0;i<dane.length;i++){
                    if(seria==dane[i].getRoundNumber()){
                        nrSerii++;
                    }
                    if(nrSerii==koniec) return i;
                }
                return sum;
            }

            private View.OnClickListener setOpenTrainingResultOnClickListener(final OldTraining[][] currentTraining, final OldTraining[][] lastTraining) {
                View.OnClickListener lisener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (lastTraining==null) {
                            StatisticsCompareTraining.oldTrainings = new OldTraining[2][currentTraining.length][currentTraining[0].length];
                            StatisticsCompareTraining.oldTrainings[1]=currentTraining;
                            Intent intent = new Intent(Statistics.this, StatisticsCompareTraining.class);
                            startActivity(intent);
                        }
                        StatisticsCompareTraining.oldTrainings = new OldTraining[2][currentTraining.length][currentTraining[0].length];
                        StatisticsCompareTraining.oldTrainings[0]= lastTraining;
                        StatisticsCompareTraining.oldTrainings[1]=currentTraining;
                        Intent intent = new Intent(Statistics.this, StatisticsCompareTraining.class);
                        startActivity(intent);
                    }
                };
                return  lisener;
            }
        };
        return lisener;
    }

    private void sortowanieDataICzasPlusDaneStaruchTreningów(String[][] dataICzas, OldTraining[] dst) {

    }

}
