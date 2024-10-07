package pl.dsquare.gymassistant.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

import pl.dsquare.gymassistant.R;
import pl.dsquare.gymassistant.Units;
import pl.dsquare.gymassistant.db.ExerciseNamesAdapter;
import pl.dsquare.gymassistant.ui.ExerciseCreate;

public class CreateTrainingActivity extends AppCompatActivity {

    LinearLayout ll;
    LinkedList<ExerciseCreate> ecList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training);
        ll = findViewById(R.id.ll_exercises_create_training);
        ecList = new LinkedList<>();
        ecList.add((ExerciseCreate) ll.getChildAt(0));
        ecList.add((ExerciseCreate) ll.getChildAt(1));
        ecList.add((ExerciseCreate) ll.getChildAt(2));
        ExerciseNamesAdapter ena = new ExerciseNamesAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        for ( LinearLayout ll : ecList){
            AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.actv_new_exercise_add_training);
            Log.d("asdf",ena.getCount()+"");
            actv.setAdapter(ena);
        }
    }

    public void addAnotherExercise(View view) {
        ExerciseCreate ec = new ExerciseCreate(this);
        ec.setLayoutParams(new LinearLayout.LayoutParams(Units.dpToPx(this,350), ViewGroup.LayoutParams.WRAP_CONTENT));
        ec.setOrientation(LinearLayout.VERTICAL);
        ll.addView(ec,ll.getChildCount()-1);
        ecList.add(ec);
    }
}