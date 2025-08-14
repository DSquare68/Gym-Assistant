package pl.dsquare.gymassistant.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import pl.dsquare.gymassistant.R;
import pl.dsquare.gymassistant.data.TrainingRecord;
import pl.dsquare.gymassistant.db.AppDatabase;
import pl.dsquare.gymassistant.db.Exercise;
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
 /*
    public void insertExercise(boolean checked, String string,String nameT,int ID) {
        if(((AutoCompleteTextView) findViewById(R.id.actv_new_exercise_add_training)).getText()==null || ((AutoCompleteTextView) findViewById(R.id.actv_new_exercise_add_training)).getText().toString().isEmpty())
            return;
        String nameTraining = nameT;
        AppDatabase db = AppDatabase.getDatabase(getContext());
        int schemaID = 0;
        int trainingID = ID;
        int id=0;
        String name = ((AutoCompleteTextView) findViewById(R.id.actv_new_exercise_add_training)).getText().toString();
        id = db.exerciseDao().getIDByName(name);
        if (id == 0) {
            db.exerciseDao().insert(new Exercise(db.exerciseDao().getMaxID()+1,name));
            id = db.exerciseDao().getIDByName(name);
        }
        if (checked) {
            schemaID=0;
        }
        else {
            schemaID = db.trainingDao().getIDSchema(string);
        }
        if(findViewById(R.id.ll_simple_series).getVisibility()==VISIBLE) {
            double d=0;
            int i = 0;
            if (((EditText) findViewById(R.id.et_weight)).getText()!=null)
                d = Double.valueOf(((EditText) findViewById(R.id.et_weight)).getText().toString());
            if(((EditText) findViewById(R.id.et_repeats)).getText()!=null)
                i=Integer.valueOf(((EditText) findViewById(R.id.et_repeats)).getText().toString());
            db.trainingDao().insert(new Training(trainingID,nameTraining, id, d,i, schemaID));
        }
        else {
            double weight;
            int reps;
            for (int i = 0; i < ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildCount(); i++) {
                if(((EditText) ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildAt(i).findViewById(R.id.et_weight)).getText()==null || ((EditText) ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildAt(i).findViewById(R.id.et_weight)).getText().toString().isEmpty()
                || ((EditText) ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildAt(i).findViewById(R.id.et_repeats)).getText()==null || ((EditText) ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildAt(i).findViewById(R.id.et_repeats)).getText().toString().isEmpty())
                    continue;
                weight = Double.valueOf(((EditText) ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildAt(i).findViewById(R.id.et_weight)).getText().toString());
                reps = Integer.valueOf(((EditText) ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildAt(i).findViewById(R.id.et_repeats)).getText().toString());
                db.trainingDao().insert(new Training(trainingID,nameTraining,id,weight,reps,i+1,schemaID));}
        }
    }
    */

    public TrainingRecord getTrainingRecord(int IDExercise, boolean isSchema,String nameSchema) {
        TrainingRecord tr = new TrainingRecord();
        tr.setID_EXERCISE_NAME(IDExercise);
        tr.setID_TRAINING(0);
        tr.setID(0);
        tr.setWEIGHT(Double.valueOf(((EditText) this.findViewById(R.id.et_weight)).getText().toString()));
        tr.setREPEAT(Integer.valueOf(((EditText) this.findViewById(R.id.et_repeats)).getText().toString()));
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        tr.setDATE_TRAINING(format.format(new Date()));
        tr.setIS_SCHEMA(isSchema ? 1 : 0);
        tr.setSCHEMA(0);
        tr.setSERIE(1);
        tr.setNAME_SCHEMA(nameSchema);
        return tr;
    }

    public Collection<? extends TrainingRecord> getTrainingRecords(int IDExercise,String nameSchema, boolean isSchema) {
        ArrayList<TrainingRecord> result = new ArrayList<>();
        TrainingRecord tr = new TrainingRecord();
        tr.setID_EXERCISE_NAME(IDExercise);
        tr.setID_TRAINING(0);
        tr.setID(0);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        tr.setDATE_TRAINING(format.format(new Date()));
        tr.setIS_SCHEMA(isSchema ? 1 : 0);
        tr.setNAME_SCHEMA(nameSchema);
        tr.setSCHEMA(0);
        int count = ((LinearLayout) findViewById(R.id.ll_extended_series_parent)).getChildCount();
        LinearLayout ll = findViewById(R.id.ll_extended_series_parent);
        for(int i=0;i<count;i++){
            if(((EditText) findViewById(R.id.actv_new_exercise_add_training)).getText()==null || ((EditText) findViewById(R.id.actv_new_exercise_add_training)).getText().toString().isEmpty())
                continue;
            TrainingRecord trClone = tr.clone();
            trClone.setWEIGHT(Double.valueOf(((EditText) ll.getChildAt(i).findViewById(R.id.et_weight)).getText().toString()));
            trClone.setREPEAT(Integer.valueOf(((EditText) ll.getChildAt(i).findViewById(R.id.et_repeats)).getText().toString()));
            trClone.setSERIE(i+1);
            result.add(trClone);
        }
        return result;
    }
}
