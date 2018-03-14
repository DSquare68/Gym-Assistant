package com.example.daniel.gymassistant;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.database.dataoldtrainings.OldTraining;
import com.example.daniel.database.dataoldtrainings.OldTrainingsDatabase;
import com.example.daniel.database.exercise.name.ExerciseDatabase;
import com.example.daniel.database.exercise.values.ExerciseValue;
import com.example.daniel.database.exercise.values.ExerciseValuesDatabase;
import com.example.daniel.database.trainings.names.TrainingName;
import com.example.daniel.database.trainings.names.TrainingNamesDatabase;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.database.trainings.trainingvalues.TrainingValuesDatabase;
import com.example.daniel.procedures.DateTraining;
import com.example.daniel.values.Resolution;
import com.example.daniel.values.SettingsValues;

import java.util.Date;

public class StartTraining extends AppCompatActivity {
    LinearLayout[] exercises = new LinearLayout[20];
    static TrainingValue trainingValue;
    TrainingName[] trainingNames;
    TrainingNamesDatabase trainingNamesDatabase;
    DateTraining dataTraining;
    ExerciseValuesDatabase exerciseValuesDatabase;
    TrainingValuesDatabase trainingValuesDatabase;
    static ExerciseValue[] exerciseValues;
    ScrollView scrollView;
    Toolbar mActionBarToolbar;
    LinearLayout parentLayout;
    Dialog dialog;
    LinearLayout stopWatch;
    RelativeLayout finishR;
    OldTraining[][] oldTrainings;
    boolean isStopWatcherVisible =false;
    boolean isKeyboardVisible =false;
    boolean isNewTrainingOpening =false;
    boolean newTraining=false;
    boolean firstOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_start_training);
        trainingNamesDatabase = new TrainingNamesDatabase(this);
        dataTraining = new DateTraining(this);
        exerciseValuesDatabase = new ExerciseValuesDatabase(this);
        trainingValuesDatabase = new TrainingValuesDatabase(this);
        dialog = new Dialog(StartTraining.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setComponents();
        switch (SettingsValues.getValue(SettingsValues.TRAINING_START_MODE, getApplicationContext())) {
            case 1:
                readAndShowTraining();
                isTrainingAvailable();
                setToolbar();
                firstOpen=true;
                break;
            case 2:
                isTrainingAvailable();
                setDialog();
                break;
        }
        ((LinearLayout)finishR.getParent()).removeView(finishR);
        addContentView(finishR,finishR.getLayoutParams());
        resizeComponents();
    }

    private void setToolbar() {
        if(trainingValue!=null&&trainingValue.getTrainingName()!=null) mActionBarToolbar.setTitle(trainingValue.getTrainingName()); else mActionBarToolbar.setTitle(R.string.training);
       setSupportActionBar(mActionBarToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_training, menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(isStopWatcherVisible){
                    isStopWatcherVisible =false;
                    stopWatch.setVisibility(View.GONE);
                } else{
                    isStopWatcherVisible =true;
                    stopWatch.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                isNewTrainingOpening =true;
                setDialog();
                return true;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                clearData();
                return false;
            }
        });
        menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(StartTraining.this, MainActivity.class);
                startActivity(intent);
                readDateFromExercises(new OldTrainingsDatabase(getApplicationContext(),String.valueOf(mActionBarToolbar.getTitle().toString())));
                return false;
            }
        });

        return true;
    }
    private void clearData() {
        LinearLayout scrollLL =(LinearLayout) scrollView.getChildAt(0);
        for (int i=0;i<scrollLL.getChildCount();i++) {
            LinearLayout exercise = (LinearLayout) scrollLL.getChildAt(i);
            LinearLayout roundsLL = exercise.findViewById(R.id.rounds);
            for(int j=0;j<roundsLL.getChildCount();j++) {
                LinearLayout round = (LinearLayout) roundsLL.getChildAt(j);
                EditText weight = round.findViewById(R.id.weight);
                EditText reps =  round.findViewById(R.id.reps);
                weight.setText("");
                reps.setText("");
            }
        }
    }

    private void isTrainingAvailable() {
        if(trainingNames ==null){
            Toast toast= Toast.makeText(getApplicationContext(),R.string.communique_no_trainings,Toast.LENGTH_SHORT);
            toast.show();
            super.onBackPressed();
        }
    }
    private void setComponents(){
        parentLayout = (LinearLayout) View.inflate(this, R.layout.activity_start_training, null);
        finishR =parentLayout.findViewById(R.id.finished_relative_layout);
        finishR.setVisibility(View.GONE);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        mActionBarToolbar = parentLayout.findViewById(R.id.toolbar_start_training);
        trainingNames = trainingNamesDatabase.getAll();
        stopWatch = parentLayout.findViewById(R.id.stopwatch);
        scrollView = parentLayout.findViewById(R.id.scrollView_start_training);

        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setFillViewport(true);
        setContentView(parentLayout);

    }
    private void setDialog(){
        dialog.setContentView(R.layout.dialog_radio_group_cancel_ok);
        dialog.setTitle(R.string.choose_training);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group_start_mode);
        final TrainingName[] s = trainingNamesDatabase.getAll();
        final RadioButton[] radioButtons = new RadioButton[s.length+1];
        for(int i=0;i<s.length;i++){
            radioButtons[i] = new RadioButton(this);
            radioButtons[i].setText(s[i].getName());
            radioGroup.addView(radioButtons[i]);
        }
        radioButtons[s.length] = new RadioButton(this);
        radioButtons[s.length].setText(getResources().getString(R.string.new_training));
        radioGroup.addView(radioButtons[s.length]);
        Button okButton =(Button)  dialog.findViewById(R.id.ok);
        Button cancelButton=(Button) dialog.findViewById(R.id.cancel);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                if(radioButtonID!=-1) {
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int idx = radioGroup.indexOfChild(radioButton);
                    if(idx==s.length){
                        newTraining =true;
                        setComponents();
                        readAndShowTraining();
                        dialog.dismiss();
                        return;

                    }
                    if(isNewTrainingOpening){
                        parentLayout.removeAllViews();
                        ((ViewGroup ) parentLayout.getParent()).removeAllViews();
                        setComponents();
                    }
                    isNewTrainingOpening =false;
                    trainingValue = trainingValuesDatabase.getByTrainingID(s[idx].getID());
                    readAndShowTraining();
                    setToolbar();
                    dialog.dismiss();
                }
                firstOpen=true;

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartTraining.this, MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    String trainingName ="";
    private void getTrainingName() {
        DateTraining dt = new DateTraining(this);
        trainingName =dt.getDate(new Date());
    }
    private void readAndShowTraining(){
        if(trainingValue ==null&&!newTraining){
            trainingValue = dataTraining.getNearestTraining();
            if(trainingValue ==null) return;
        } else if(newTraining){

            trainingValue = new TrainingValue(0,"","","",0,0,"","","",0,0);

        }
        if(!newTraining){
            exerciseValues = exerciseValuesDatabase.getByID(trainingValue.getTrainingId());
        } else{
            exerciseValues = new ExerciseValue[6];
            for(int i=0;i<6;i++){
                exerciseValues[i]= new ExerciseValue(0,0,0,0,0,0);
            }
        }
        DateTraining dateTraining = new DateTraining(this);
        if(!newTraining) oldTrainings = dateTraining.lastTraining(trainingNamesDatabase.getTrainingName(trainingValue.getTrainingId()).getName(), this);
        for(int i = 0; i< exerciseValues.length; i++) {
            exercises[i] = createExercise();
            fillRoundsWithHints(i,exercises[i]);
        }
        LinearLayout LL =(LinearLayout) scrollView.getChildAt(0);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        TextView Finished = finishR.findViewById(R.id.finished_text);
        if(newTraining) finishR.findViewById(R.id.add_exercise_button).setVisibility(View.VISIBLE);
        Finished.setOnClickListener(setFinishedOnClickListener());
        for(int i = 0; i< exerciseValues.length; i++) {
            LL.addView(exercises[i]);
        }
    }

    private void fillRoundsWithHints(int i, LinearLayout exercise) {
        ((TextView) exercise.findViewById(R.id.exercise_name)).setText(exerciseValues[i].getName());
        HorizontalScrollView horizontalScrollView= exercise.findViewById(R.id.horizontal_scroll_view);
        horizontalScrollView.setFillViewport(true);
        LinearLayout rounds = exercise.findViewById(R.id.rounds);
        for(int j=0;j<(exerciseValues[i].getRoundNumber()==0 ? trainingValue.getRoundsNumber() : exerciseValues[i].getRoundNumber());j++){
            LayoutInflater.from(this).inflate(R.layout.start_training_exercise_round,(LinearLayout) horizontalScrollView.findViewById(R.id.rounds),true);
            LinearLayout container =(LinearLayout) rounds.getChildAt(j);
            TextView roundNumber =container.findViewById(R.id.number_of_round);
            roundNumber.setText(String.valueOf(j+1));
            EditText weight = container.findViewById(R.id.weight);
            EditText reps = container.findViewById(R.id.reps);
            switch(SettingsValues.getValue(SettingsValues.DISPLAY_TIPS,getApplicationContext())){
                case 1:
                    weight.setHint(R.string.weight);
                    reps.setHint(R.string.reps);
                    break;
                case 2:
                    weight.setHint(String.valueOf(exerciseValues[i].getWeight()));
                    reps.setHint(String.valueOf(exerciseValues[i].getReps()));
                    break;
                case 3:
                    if((oldTrainings==null)||(oldTrainings.length<i)|| oldTrainings[i].length>j&& oldTrainings[i][j]==null ||oldTrainings[i].length<=j)weight.setHint(String.valueOf(R.string.weight));else
                        weight.setHint(String.valueOf(oldTrainings[i][j].getWeight()));
                    if((oldTrainings==null)||(oldTrainings.length<i)|| oldTrainings[i].length>j&& oldTrainings[i][j]==null ||oldTrainings[i].length<=j)reps.setHint(String.valueOf(R.string.reps)); else reps.setHint(String.valueOf(oldTrainings[i][j].getReps()));
                    break;
            }
        }
    }

    private LinearLayout createExercise(){
        return (LinearLayout) LayoutInflater.from(this).inflate(R.layout.start_training_exercise,null,false);
    }
    private View.OnClickListener setFinishedOnClickListener() {
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newTraining){
                    final Dialog d = new Dialog(v.getContext());
                    d.setTitle(getResources().getString(R.string.what_training_name_question));
                    d.setContentView(R.layout.start_training_choose_training);
                    final RadioGroup radioGroup = d.findViewById(R.id.radio_group_choose_training);
                    String[] s = getResources().getStringArray(R.array.add_training_create_name);
                    final RadioButton[] radioButtons = new RadioButton[s.length+1];
                    for(int k=0;k<s.length;k++){
                        radioButtons[k] = new RadioButton(v.getContext());
                        radioButtons[k].setText(s[k]);
                        radioGroup.addView(radioButtons[k]);
                    }
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                            View radioButton = radioGroup.findViewById(checkedId);
                            int idx = radioGroup.indexOfChild(radioButton);
                            if(idx==1) d.findViewById(R.id.training_name).setVisibility(View.VISIBLE); else  d.findViewById(R.id.training_name).setVisibility(View.GONE);
                        }
                    });
                    (d.findViewById(R.id.ok_button)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int radioButtonID = radioGroup.getCheckedRadioButtonId();
                            View radioButton = radioGroup.findViewById(radioButtonID);
                            int idx = radioGroup.indexOfChild(radioButton);
                            if(idx==0){
                                getTrainingName();
                            } else{
                                readTrainingNameFromTextView((EditText) d.findViewById(R.id.training_name));
                            }
                            readDateFromExercises(new OldTrainingsDatabase(getApplicationContext(), trainingName));
                            d.dismiss();

                        }


                    });
                    Button cancel = d.findViewById(R.id.cancel_button);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                        }
                    });
                    d.show();
                } else {
                    Intent intent = new Intent(StartTraining.this, MainActivity.class);
                    startActivity(intent);
                    readDateFromExercises(new OldTrainingsDatabase(getApplicationContext(), String.valueOf(mActionBarToolbar.getTitle().toString())));
                }
            }
        };
        return click;
    }
    private void readDateFromExercises(OldTrainingsDatabase dstd) {
        LinearLayout ll = (LinearLayout) scrollView.getChildAt(0);
        DateTraining dt = new DateTraining(getApplicationContext());
        ExerciseDatabase exerciseDatabase = new ExerciseDatabase(getApplicationContext());
        Date date = new Date();
        updateTime = timeInMilliseconds;
        int secs =(int) (updateTime/1000);
        int mins=secs/60;
        secs%=60;
        int hour=mins/60;
        if(newTraining){
            trainingNamesDatabase.addTrainingName(new TrainingName(trainingName));
            String weekDay="";
            String[] arrayShortDays = getResources().getStringArray(R.array.short_week_days);
            //getting short day (mon, thu, sat)
            for(int i=0;i<6;i++){
                if((new Date()).getDay()==i){
                    weekDay=arrayShortDays[(i==0 ? 6 : --i)];
                }
            }
            //TODO SERIES
            trainingValuesDatabase.add(new TrainingValue(trainingName,trainingNamesDatabase.getIndex(trainingName),weekDay,"SERIES","",3,ll.getChildCount(), dataTraining.getDate(date), dataTraining.getDate(date), dataTraining.getDate(date),0,updateTime));
        }
        //exercises
        for(int i=0;i<ll.getChildCount();i++){
            LinearLayout exercise =(LinearLayout) ll.getChildAt(i);
            LinearLayout rounds = exercise.findViewById(R.id.rounds);
            TextView exerciseName = exercise.findViewById(R.id.exercise_name);
            //rounds of exercises
            for(int j=0;j<rounds.getChildCount();j++){
                LinearLayout round =(LinearLayout) rounds.getChildAt(j);
                TextView weight = round.findViewById(R.id.weight);
                TextView reps = round.findViewById(R.id.reps);
                if(newTraining){
                    dstd.addOldTraining(new OldTraining(trainingName,trainingNamesDatabase.getIndex(trainingName),exerciseName.getText().toString(),exerciseDatabase.getIndex(exerciseName.getText().toString()),dt.getDate(date),String.format("%01d",date.getHours())+":"+String.format("%02d",date.getMinutes())+":"+String.format("%02d",date.getSeconds()),String.format("%01d",hour)+":"+String.format("%02d",mins)+":"+String.format("%02d",secs) ,j+1,Integer.valueOf( reps.getText().toString()),Double.valueOf(weight.getText().toString())));
                }
                else {
                    Log.d("exercise number",String.valueOf(exerciseDatabase.getIndex(exerciseName.getText().toString())));
                    dstd.addOldTraining(new OldTraining(mActionBarToolbar.getTitle().toString(),trainingNamesDatabase.getIndex(mActionBarToolbar.getTitle().toString()),exerciseName.getText().toString(),exerciseDatabase.getIndex(exerciseName.getText().toString()),dt.getDate(date),String.format("%01d",date.getHours())+":"+String.format("%02d",date.getMinutes())+":"+String.format("%02d",date.getSeconds()),String.format("%01d",hour)+":"+String.format("%02d",mins)+":"+String.format("%02d",secs) ,j+1,Integer.valueOf( reps.getText().toString()),Double.valueOf(weight.getText().toString())));
                }
            }
        }
    }

    private void readTrainingNameFromTextView(EditText et) {
        trainingName =et.getText().toString();
    }

    private void resizeComponents(){
        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousHeight =0;
            boolean stopWatchSupport =false;

            @Override
            public void onGlobalLayout() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Rect measureRect = new Rect();
                        parentLayout.getWindowVisibleDisplayFrame(measureRect);
                        int keypadHeight = parentLayout.getRootView().getHeight() - measureRect.bottom;
                        Log.d("wymiary",String.valueOf(scrollView.findViewById(R.id.linearLayout_scrollView).getHeight())+"    "+String.valueOf(scrollView.getHeight())+"      "+String.valueOf(keypadHeight)+"     " + String.valueOf(previousHeight));
                        if(keypadHeight!= previousHeight) {
                            scrollView.findViewById(R.id.linearLayout_scrollView).setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (scrollView.findViewById(R.id.linearLayout_scrollView).getHeight() - keypadHeight + previousHeight)));
                            scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (scrollView.getHeight() - keypadHeight + previousHeight)));
                            previousHeight = keypadHeight;
                        }
                        if(stopWatchSupport != isStopWatcherVisible) {
                            if (isStopWatcherVisible) {
                                scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (scrollView.getHeight() - keypadHeight -stopWatch.getHeight()+ previousHeight)));
                            }else{
                                scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (scrollView.getHeight() - keypadHeight +stopWatch.getHeight()+ previousHeight)));
                            }
                            stopWatchSupport = isStopWatcherVisible;
                        }
                        if(firstOpen){
                            finishR.setVisibility(View.VISIBLE);
                            Log.d("first",String.valueOf(  ((TextView)finishR.findViewById(R.id.finished_text)).getLineHeight() ));
                            scrollView.findViewById(R.id.linearLayout_scrollView).setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (scrollView.findViewById(R.id.linearLayout_scrollView).getHeight() -((TextView)finishR.findViewById(R.id.finished_text)).getLineHeight())));
                            scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (scrollView.getHeight()-((TextView)finishR.findViewById(R.id.finished_text)).getLineHeight())));
                            firstOpen=false;

                        }
                    }
                }, 10);

            }



        });
    }
    long startTime=0L, timeInMilliseconds =0L, timeSwapBuff=0L,updateTime=0L;
    boolean clockTicking =false;
    TextView timer;
    Handler handler = new Handler();
    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwapBuff+ timeInMilliseconds;
            int secs =(int) (updateTime/1000);
            int mins=secs/60;
            secs%=60;
            int hour=mins/60;
            timer.setText(String.format("%01d",hour)+":"+String.format("%02d",mins)+":"+String.format("%02d",secs));
            handler.postDelayed(this,0);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        exerciseValues =null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.dismiss();
    }

    public void pause(View view) {
        if(clockTicking) {
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimerThread);
            clockTicking =false;
        }
    }

    public void play(View view) {
        if(!clockTicking){
            timer =(TextView) parentLayout.findViewById(R.id.stopwatch);
            clockTicking =true;
            startTime= SystemClock.uptimeMillis();
            handler.postDelayed(updateTimerThread,0);
        }
    }
    public void addExercise(View view) {
        LinearLayout LL = createExercise();
        ((LinearLayout) scrollView.getChildAt(0)).addView(LL);
    }

    public void deleteRound(View view) {
        LinearLayout LL = (LinearLayout) view.getParent().getParent();
        LinearLayout LL3 =LL.findViewById(R.id.rounds);
        int number = LL3.getChildCount();
        if (number>0)
            LL3.removeViewAt(number-1);
    }

    public void addRound(View view) {
        LinearLayout LL = (LinearLayout) view.getParent().getParent();
        LinearLayout LL3 =LL.findViewById(R.id.rounds);
        LayoutInflater.from(this).inflate(R.layout.start_training_exercise_round,LL3,true);
        ((TextView) LL3.getChildAt(LL3.getChildCount()-1).findViewById(R.id.number_of_round)).setText(String.valueOf(LL3.getChildCount()));
    }


}
