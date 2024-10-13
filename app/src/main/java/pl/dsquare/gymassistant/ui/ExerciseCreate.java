package pl.dsquare.gymassistant.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import pl.dsquare.gymassistant.R;
import pl.dsquare.gymassistant.db.AppDatabase;
import pl.dsquare.gymassistant.db.Training;

public class ExerciseCreate extends LinearLayout {
    public ExerciseCreate(Context context){
        super(context);
        inflate(context, R.layout.add_training_component_basic,this);
    }
    public ExerciseCreate(Context context, AttributeSet set) {
        super(context,set);
        inflate(context, R.layout.add_training_component_basic,this);
    }

    public void insertExercise(boolean checked, String string) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        String name = ((AutoCompleteTextView) findViewById(R.id.actv_new_exercise_add_training)).getText().toString();
        int id = db.exerciseDao().getIDByName(name);
        if(id==0) {
            db.exerciseDao().addExercise(name);
            id = db.exerciseDao().getIDByName(name);
        }
        int schemaID =0;
        int trainingID = db.trainingDao().getMaxID();
        if (checked)
            trainingID = db.trainingDao().getMAXSchemaID();
        else
            trainingID = db.trainingDao().getIDSchema(string);
        if(findViewById(R.id.cb_new_schema).getVisibility()==VISIBLE)
            db.trainingDao().insert(new Training(trainingID,id,Double.valueOf(((EditText) findViewById(R.id.et_weight)).getText().toString()),Integer.valueOf(((EditText) findViewById(R.id.et_repeats)).getText().toString()),schemaID));
        else {
            double weight;
            int reps;
            for (int i = 0; i < ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildCount(); i++) {
                weight = ((EditText) ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildAt(i).findViewById(R.id.et_weight)).getText().toString().isEmpty() ? 0 : Double.valueOf(((EditText) findViewById(R.id.et_weight)).getText().toString());
                reps = ((EditText) ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildAt(i).findViewById(R.id.et_repeats)).getText().toString().isEmpty() ? 0 : Integer.valueOf(((EditText) findViewById(R.id.et_weight)).getText().toString());
                db.trainingDao().insert(new Training(trainingID,id,weight,reps,i+1,schemaID));}
        }
    }
}
