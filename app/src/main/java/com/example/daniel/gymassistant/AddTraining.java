package com.example.daniel.gymassistant;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.daniel.database.exercise.name.Exercise;
import com.example.daniel.database.exercise.name.ExerciseDatabase;
import com.example.daniel.database.exercise.values.ExerciseValue;
import com.example.daniel.database.exercise.values.ExerciseValuesDatabase;
import com.example.daniel.database.trainings.names.TrainingNamesDatabase;
import com.example.daniel.database.trainings.trainingvalues.TrainingValue;
import com.example.daniel.extraview.ExerciseAdapter;
import com.example.daniel.extraview.Slider;
import com.example.daniel.values.AddTrainingValues;
import com.example.daniel.values.Resolution;

import java.util.ArrayList;

public class AddTraining extends AppCompatActivity implements ExerciseAdapter.ItemClickCallback{
    public static  String defaultTrainingName;
    Slider slider = new Slider();
    private ArrayList listData;
    LinearLayout.LayoutParams lp;
    public static int openMode=1;
    
    private static LinearLayout recyclerViewLinearLayout;
    private static LinearLayout buttonsLayout;
    public static ExerciseAdapter adapter;
    static Toolbar mActionBarToolbar;
    public static ExerciseValue[] exerciseValues;
    public static int numberOfExercises;
    public static TrainingValue trainingValue;
    static Context context;

    LinearLayout parentLayout;
    static RecyclerView recyclerView;

    static ExerciseDatabase exerciseDatabase;
    static TrainingNamesDatabase trainingNamesDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        defaultTrainingName =  getResources().getString(R.string.training);
        exerciseDatabase = new ExerciseDatabase(getApplicationContext());
        trainingNamesDatabase = new TrainingNamesDatabase(getApplicationContext());
        parentLayout =(LinearLayout) View.inflate(this,R.layout.activity_add_training,null);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setToolbar();
        switch(openMode){
            case AddTrainingValues.OPEN_FROM_MAIN_MENU: openFromMainMenu();
                break;
            case AddTrainingValues.OPEN_FROM_SCHEDULE: openFromTimeTable();
                break;
            case AddTrainingValues.OPEN_FROM_PROGRESS:
                openFromProgress();
                break;
        }
        setContentView(parentLayout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        isKeyboardVisible();
    }

    private void openFromProgress() {
    }

    private void isKeyboardVisible(){

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousHeight =0;
            @Override
            public void onGlobalLayout() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Rect measureRect = new Rect(); //you should cache this, onGlobalLayout can get called often
                        parentLayout.getWindowVisibleDisplayFrame(measureRect);
                        // measureRect.bottom is the position above soft keypad
                        int keypadHeight = parentLayout.getRootView().getHeight() - measureRect.bottom;
                        if(keypadHeight!= previousHeight) {
                            recyclerViewLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (recyclerViewLinearLayout.getHeight() - keypadHeight + previousHeight)));
                            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (recyclerView.getHeight() - keypadHeight  + previousHeight)));
                            previousHeight = keypadHeight;
                        }
                    }
                },100);
            }
        });
    }

    private void openFromTimeTable() {
        //setToolbar();
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.weight = 1;
        setRecyclerView(exerciseValues);


    }

    @Override
    public void  onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_training, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setEditTextToolbar(mActionBarToolbar);
                return false;
            }
        });
        return true;
    }
    private void openFromMainMenu(){
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.weight = 1;
        setRecyclerView(null);
    }

    private void setToolbar() {
        mActionBarToolbar = (Toolbar) parentLayout.getChildAt(0);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(defaultTrainingName);
        switch(openMode) {
            case 1: mActionBarToolbar.setOnLongClickListener(setOnLongClickListenerToolbar());break;
            case 2: break;
        }
    }

    private  View.OnLongClickListener setOnLongClickListenerToolbar(){
        View.OnLongClickListener listener = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                setEditTextToolbar(mActionBarToolbar);
                return true;
            }
        };
        return listener;
    }

    public void setEditTextToolbar(final Toolbar mActionBarToolbar){
        getSupportActionBar().setTitle("");
        final EditText editText = new EditText(getApplicationContext());
        editText.setSingleLine();
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.width_edit_text_toolbar), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,LinearLayout.LayoutParams.WRAP_CONTENT  , getResources().getDisplayMetrics()));
        editText.requestFocus();
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mActionBarToolbar.setTitle(editText.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    defaultTrainingName =editText.getText().toString();
                    mActionBarToolbar.removeView(editText);
                }
            }
        });
        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        mActionBarToolbar.addView(editText,lp);

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editText.setOnFocusChangeListener(null);
                    getSupportActionBar().setTitle(editText.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    defaultTrainingName =editText.getText().toString();
                    mActionBarToolbar.removeView(editText);
                    return true;
                }
                return false;
            }


        });
    }
    private void setRecyclerView(ExerciseValue[] exerciseValues){
        listData = new ArrayList();
        if(exerciseValues==null){
            for (int i=0;i<6;i++){
                listData.add(i, new ExerciseValue(0,0,0,0,0,0));
            }
        } else{
            int numberOfExercise=0;;
            for (int i = 0; i< trainingValue.getExerciseNumber(); i++){
                if(numberOfExercise<exerciseValues.length && (exerciseValues[numberOfExercise].getExerciseNumber()-1)==i) {
                    listData.add(i, exerciseValues[numberOfExercise]);
                    numberOfExercise++;
                } else{
                    listData.add(i, new ExerciseValue(0,0,0,0,0,0));
                }

            }
        }
        recyclerViewLinearLayout = (LinearLayout) View.inflate(this, R.layout.add_training_recycler_view, null);
        buttonsLayout = (LinearLayout) View.inflate(this, R.layout.add_training_buttons, null);
        recyclerView = (RecyclerView) recyclerViewLinearLayout.getChildAt(0);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setScrollContainer(true);
        LinearLayout.LayoutParams lpScroll=null;

        buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        adapter = new ExerciseAdapter(listData, this);
        recyclerView.setAdapter(adapter);
        if(lpScroll!=null){
            parentLayout.addView(recyclerViewLinearLayout, lpScroll);
        } else{
            parentLayout.addView(recyclerViewLinearLayout, lp);
        }
        //lp.gravity=Gravity.CENTER_HORIZONTAL;
        parentLayout.addView(buttonsLayout, buttonsLayout.getLayoutParams());
        if(!Resolution.hasSoftKeys){
            Rect measureRect = new Rect(); //you should cache this, onGlobalLayout can get called often
            recyclerViewLinearLayout.getWindowVisibleDisplayFrame(measureRect);
            lpScroll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, recyclerViewLinearLayout.getRootView().getHeight() - measureRect.bottom, getResources().getDisplayMetrics()));
        } else{
            Rect measureRect = new Rect(); //you should cache this, onGlobalLayout can get called often
            recyclerViewLinearLayout.getWindowVisibleDisplayFrame(measureRect);
            TypedValue tv = new TypedValue();
            int actionBarHeight=0;
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            }
            lpScroll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int) (measureRect.bottom/3.2), getResources().getDisplayMetrics()));
        }
        recyclerViewLinearLayout.setLayoutParams(lpScroll);
        recyclerView.setLayoutParams(lpScroll);
        if(exerciseValues==null) {
            setItemTouchHelper();
        }
    }

    private void setItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean isLongPressDragEnabled() {
                        return true;
                    }

                    @Override
                    public boolean isItemViewSwipeEnabled() {
                        return true;
                    }

                    @Override
                    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                        return makeMovementFlags(dragFlags, swipeFlags);
                    }

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                        if(actionState == ItemTouchHelper.ACTION_STATE_IDLE){
                            isKeyboardVisible();
                        }
                        super.onSelectedChanged(viewHolder, actionState);

                    }

                    @Override
                    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        super.clearView(recyclerView, viewHolder);


                    }
                };
        return simpleItemTouchCallback;
    }


    private void addItemToList() {

        ExerciseValue item = new ExerciseValue(0,0,0,0,0,0);
        adapter.exerciseList.add(adapter.exerciseList.size(),item);
        adapter.notifyItemInserted(adapter.exerciseList.indexOf(item));
    }

    private void moveItem(int oldPos, int newPos) {
        ExerciseValue item = ExerciseAdapter.exerciseList.get(oldPos);
        ExerciseAdapter.exerciseList.remove(oldPos);
        ExerciseAdapter.exerciseList.add(newPos, item);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem(final int position) {
        ExerciseAdapter.exerciseList.remove(position);
        adapter.notifyItemRemoved(position);
    }
    public void addExercise(View view){
        addItemToList();
    }

    @Override
    public void onItemClick(int p) {
        Exercise item = (Exercise) listData.get(p);

        Intent i = new Intent(this, AddTraining.class);

        startActivity(i);
    }
    public static ArrayList<Exercise> exercises = new ArrayList<Exercise>();
    public  static ArrayList<ExerciseValue> exerciseValuesList = new ArrayList<ExerciseValue>();
    @Override
    public void onSecondaryIconClick(int p) {

    }
    public static void saveData(){
        saveDataDoArrayList();
        ExerciseDatabase cw = new ExerciseDatabase(recyclerView.getContext());
        TrainingNamesDatabase nt = new TrainingNamesDatabase(recyclerView.getContext());
        ExerciseValuesDatabase wc = new ExerciseValuesDatabase(recyclerView.getContext());

        for(int i = 0; i< exercises.size(); i++){
            cw.addExercise(exercises.get(i));
        }

        /*switch (openMode){
            case AddTrainingValues.OPEN_FROM_MAIN_MENU: nt.addTrainingName(defaultTrainingName);break;
        }*/
        switch (openMode) {
            case AddTrainingValues.OPEN_FROM_MAIN_MENU:
                for (int i = 0; i < exerciseValuesList.size(); i++) {
                    wc.addExerciseValue(exerciseValuesList.get(i));
                }
                break;
            case AddTrainingValues.OPEN_FROM_SCHEDULE:
                wc.editExerciseValue(exerciseValuesList.get(0));
                for (int i = 1; i < exerciseValuesList.size(); i++) {
                    wc.addExerciseValue(exerciseValuesList.get(i));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private static void saveDataDoArrayList(){
        String exerciseName;
        int roundNumber ;
        double weight ;
        int reps;
        int exerciseNumber=0;
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
        for(int i=0;i<adapter.getItemCount();i++){
            boolean nameIsEntered=false; boolean dataAreEntered=false;
            if(!adapter.exerciseList.get(i).getName().equals("")){ exerciseName=adapter.exerciseList.get(i).getName();nameIsEntered=true;dataAreEntered=true;} else exerciseName="";
            if((adapter.exerciseList.get(i).getRoundNumber()!=0)){ roundNumber=adapter.exerciseList.get(i).getRoundNumber();dataAreEntered=true;} else roundNumber = 0;
            if((adapter.exerciseList.get(i).getWeight()!=0.0)){ weight=adapter.exerciseList.get(i).getWeight();dataAreEntered=true;} else weight = 0;
            if((adapter.exerciseList.get(i).getReps()!=0)){ reps=adapter.exerciseList.get(i).getReps(); dataAreEntered=true;}else reps=0;
            if(nameIsEntered) {
                exercises.add(new Exercise(exerciseName));
            }
            if(dataAreEntered){
                exerciseNumber++;
                exerciseValuesList.add(new ExerciseValue(exerciseDatabase.getIndex(exerciseName), trainingNamesDatabase.getIndex(defaultTrainingName),exerciseNumber, roundNumber , weight, reps));
                numberOfExercises++;
            }
        }


    }
    public void nextLayout(View view) {
        switch(openMode) {

            case AddTrainingValues.OPEN_FROM_MAIN_MENU:
                if (trainingNamesDatabase.isTrainingNameRepeated(defaultTrainingName)) {
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_add_training_training_exist), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                break;
        }
        setFreezeTextView();
        //saveDataDoArrayList();
        Intent intentMain = new Intent(AddTraining.this ,
                AddTrainingSettings.class);
        AddTraining.this.startActivity(intentMain);
    }

    private void setFreezeTextView() {
        EditText numberOfRoundsET, weightET, repsET;
        AutoCompleteTextView ACT;
        for(int i=0;i<recyclerView.getChildCount();i++) {
            LinearLayout LL = (LinearLayout) recyclerView.getChildAt(i);
            ACT = (AutoCompleteTextView) LL.getChildAt(0);
            LinearLayout LL2 = (LinearLayout) LL.getChildAt(2);
            numberOfRoundsET = (EditText) LL2.getChildAt(1);
            weightET = (EditText) LL2.getChildAt(3);
            repsET = (EditText) LL2.getChildAt(5);
            repsET.setFreezesText(true);
            numberOfRoundsET.setFreezesText(true);
            weightET.setFreezesText(true);
            ACT.setFreezesText(true);
        }
    }
}
