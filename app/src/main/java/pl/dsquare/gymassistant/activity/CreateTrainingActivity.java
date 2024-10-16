package pl.dsquare.gymassistant.activity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

import pl.dsquare.gymassistant.R;
import pl.dsquare.gymassistant.Units;
import pl.dsquare.gymassistant.db.AppDatabase;
import pl.dsquare.gymassistant.db.ExerciseNamesAdapter;
import pl.dsquare.gymassistant.db.Training;
import pl.dsquare.gymassistant.ui.ExerciseCreate;
import pl.dsquare.gymassistant.ui.Serie;

public class CreateTrainingActivity extends AppCompatActivity {

    LinearLayout ll;
    LinkedList<ExerciseCreate> ecList;
    LinkedList<Training> trainings;
    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training);
        db = AppDatabase.getDatabase(this);
        initExerciseList();
        new Thread(()->initSpinner()).start();
        //((CheckBox) findViewById(R.id.cb_new_schema)).setOnCheckedChangeListener((buttonView, isChecked) -> newSchemaSelected(isChecked));
    }

    private void initSpinner() {
        Spinner s = findViewById(R.id.spinner_old_schemas);
        s.setAdapter(new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,db.trainingDao().getAllSchemaNames()));
    }


    private void initExerciseList() {
        ll = findViewById(R.id.ll_exercises_create_training);
        ecList = new LinkedList<>();
        ecList.add((ExerciseCreate) ll.getChildAt(0));
        ecList.add((ExerciseCreate) ll.getChildAt(1));
        ecList.add((ExerciseCreate) ll.getChildAt(2));
        ecList.add((ExerciseCreate) ll.getChildAt(3));
        ecList.add((ExerciseCreate) ll.getChildAt(4));
        ecList.add((ExerciseCreate) ll.getChildAt(5));
        ExerciseNamesAdapter ena = new ExerciseNamesAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        for ( LinearLayout layout : ecList){
            AutoCompleteTextView actv = ( AutoCompleteTextView) layout.findViewById(R.id.actv_new_exercise_add_training);
            actv.setAdapter(ena);
            layout.findViewById(R.id.ib_advance_exercise).setOnClickListener((v)->extendedVersionExercise(layout,(ImageButton) v));
            layout.findViewById(R.id.ib_delete_serie).setOnClickListener((view)->((LinearLayout)layout.getParent()).removeView(layout));
        }
    }

    private void extendedVersionExercise(LinearLayout ll , ImageButton v) {
        ll.findViewById(R.id.ll_simple_series).setVisibility(View.GONE);
        ll.findViewById(R.id.ll_extended_series).setVisibility(View.VISIBLE);
        Serie s;
        s = new Serie(getApplicationContext());
        s.setOrientation(LinearLayout.VERTICAL);
        s.setLayoutParams(new LinearLayout.LayoutParams(Units.dpToPx(this,100), ViewGroup.LayoutParams.WRAP_CONTENT));
        for(int i=0;i<3;i++){
            ((LinearLayout) ll.findViewById(R.id.ll_extended_series_parent)).addView(s);
            s = new Serie(getApplicationContext());
            s.setOrientation(LinearLayout.VERTICAL);
            s.setLayoutParams(new LinearLayout.LayoutParams(Units.dpToPx(this,100), ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        Serie serie = s;
        ll.findViewById(R.id.button_add_another_series).setOnClickListener((view)->((LinearLayout) ll.findViewById(R.id.ll_extended_series_parent)).addView(serie));
        int y = ll.getLayoutParams().height;
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,Units.dpToPx(this,150)));
        ll.invalidate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_training_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void addAnotherExercise(View view) {
        ExerciseCreate ec = new ExerciseCreate(this);
        ec.setLayoutParams(new LinearLayout.LayoutParams(Units.dpToPx(this,350), ViewGroup.LayoutParams.WRAP_CONTENT));
        ec.setOrientation(LinearLayout.VERTICAL);
        ec.findViewById(R.id.ib_advance_exercise).setOnClickListener((v)->extendedVersionExercise(ec,(ImageButton) v));
        ec.findViewById(R.id.ib_delete_serie).setOnClickListener((v)->((LinearLayout)ec.getParent()).removeView(ec));
        ll.addView(ec,ll.getChildCount());
        ecList.add(ec);
    }

    public void addTraining(MenuItem item) {
        ecList.forEach(e->e.insertExercise(((CheckBox) findViewById(R.id.cb_new_schema)).isChecked(),((Spinner) findViewById(R.id.spinner_old_schemas)).getSelectedItem().toString()));
    }
}